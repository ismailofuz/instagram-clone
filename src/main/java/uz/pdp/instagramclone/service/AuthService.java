package uz.pdp.instagramclone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.instagramclone.entity.Attachment;
import uz.pdp.instagramclone.entity.User;
import uz.pdp.instagramclone.payload.ApiResponse;
import uz.pdp.instagramclone.payload.ShowUser;
import uz.pdp.instagramclone.payload.UserDto;
import uz.pdp.instagramclone.repository.AttachmentRepository;
import uz.pdp.instagramclone.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }




    public ApiResponse searchByUserName(String username, Pageable pageable) {
        Page<User> allByNameContains = userRepository.findAllByUsernameContains(username, pageable);

        return new ApiResponse("Found",true,allByNameContains);
    }

    public ApiResponse getOneUser(Long current_user_id, Long user_profile) {
        if (userRepository.existsById(current_user_id)) {
            if (userRepository.existsById(user_profile)) {
                User currentUser = userRepository.getById(current_user_id);
                User userProfile = userRepository.getById(user_profile);

                if (currentUser.equals(userProfile)){
                    return new ApiResponse("This is user profile",true,currentUser);
                }

                ShowUser showUser = new ShowUser();
                showUser.setUser(userProfile);
                showUser.setFollowed(currentUser.getFollowing().contains(userProfile));
                return new ApiResponse("User found",true,showUser);
            }
        }
        return new ApiResponse("Something went wrong",false);
    }

    public ApiResponse verifyEmail(String email, String emailCode) {

        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail.isPresent()) {
            User user = userByEmail.get();
            if (user.getEmailCode().equals(emailCode)){
                user.setEnabled(true);
                userRepository.save(user); // user update bo'ldi
                return new ApiResponse("Email verified successfully",true,user);
            }
            userRepository.delete(user); // user o'chadi
        }
        return new ApiResponse("Something went wrong",false);
    }

    public ApiResponse follow(Long user_id, Long following_user_id) {
        if (userRepository.findById(user_id).isPresent()) {
            User currentUser = userRepository.getById(user_id);
            if (userRepository.existsById(following_user_id)){
                User user = userRepository.getById(following_user_id);

                Set<User> followings = currentUser.getFollowing();
                if (!followings.add(user)) {
                    followings.remove(user);
                }
                userRepository.save(user);
                return new ApiResponse("Successfully done",true);
            }
        }
        return new ApiResponse("User not found with current id",false);
    }

    public ApiResponse editProfile(UserDto dto) {
        Optional<User> optionalUser = userRepository.findByUsername(dto.getUsername());
        User user = optionalUser.get();

        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if (user.getId() != principal.getId()){
            return new ApiResponse("This username already taken",false);
        }


        principal.setEmail(dto.getEmail());
        principal.setBio(dto.getBio() !=null ? dto.getBio() : "");
        principal.setName(dto.getName());
        principal.setWebsite(dto.getWebsite());

        User save = userRepository.save(principal);

        return new ApiResponse("User is updated successfully",true,save);
    }

    public ApiResponse editProfilePhoto(Long attachment_id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // checking attachment exists or not ?


        Optional<Attachment> optionalAttachment = attachmentRepository.findById(attachment_id);

        if (optionalAttachment.isPresent()) {
            Attachment newPhoto = optionalAttachment.get();

            principal.setProfilePhoto(newPhoto);
            User save = userRepository.save(principal);

            return new ApiResponse("User photo updated",true,save);
        } else {
            return new ApiResponse("Attachment not found",false);
        }
    }
}
