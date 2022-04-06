package uz.pdp.instagramclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.instagramclone.entity.Attachment;
import uz.pdp.instagramclone.entity.Post;
import uz.pdp.instagramclone.entity.User;
import uz.pdp.instagramclone.payload.ApiResponse;
import uz.pdp.instagramclone.payload.PostDto;
import uz.pdp.instagramclone.payload.ShowPost;
import uz.pdp.instagramclone.repository.AttachmentRepository;
import uz.pdp.instagramclone.repository.PostRepository;
import uz.pdp.instagramclone.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    public ApiResponse getOnePost(Long id, Long user_id) {
        if (postRepository.existsById(id)) {
            Post post = postRepository.getById(id);

            ShowPost showPost = new ShowPost();

            // checkings :
            Optional<User> userOptional = userRepository.findById(user_id);
            if (userOptional.isPresent()) {


                User user = userOptional.get();
                boolean liked = user.getLikedPosts().contains(post);
                boolean saved = user.getSavedPosts().contains(post);

                showPost.setPost(post);
                showPost.setPostLikedByThisUser(liked);
                showPost.setSavedByThisUser(saved);


                // frontga ketadigan
                return new ApiResponse("Found", true, showPost);
            }


        }
        return new ApiResponse("Something went wrong", false);
    }

    public ApiResponse getUserPosts(Long user_id, Pageable pageable) {

        Page<Post> allByUser_id = postRepository.findAllByUser_Id(user_id, pageable);

        int pageNumber = pageable.getPageNumber();

        int totalPages = allByUser_id.getTotalPages();
        int number = pageable.getPageNumber();

        if (number > totalPages) {
            return new ApiResponse("wrong page number is written", false);
        }

        return new ApiResponse("Found all pages count:" + allByUser_id.getTotalPages(), true, allByUser_id);
    }

    public ApiResponse addToSavedPosts(Long post_id, Long user_id) {
        if (userRepository.existsById(user_id)) {
            User user = userRepository.getById(user_id);
            Set<Post> savedPosts = user.getSavedPosts();

            if (postRepository.existsById(post_id)) {
                Post post = postRepository.getById(post_id);

                // agar avvaldan mavjud bo'lsa uni savedddan chiqaradi saved degan tugmachasi istagramda
                if (!savedPosts.add(post)) {
                    savedPosts.remove(post);

                    userRepository.save(user);

                    return new ApiResponse("saved to saved posts", true, post);
                }
            }
        }
        return new ApiResponse("something went wrong", false);
    }

    public ApiResponse addToLikedPosts(Long post_id, Long user_id) {
        if (userRepository.existsById(user_id)) {
            User user = userRepository.getById(user_id);
            Set<Post> likedPosts = user.getLikedPosts();

            if (postRepository.existsById(post_id)) {
                Post post = postRepository.getById(post_id);

                // agar avvaldan mavjud bo'lsa uni savedddan chiqaradi saved degan tugmachasi istagramda
                if (likedPosts.add(post)) {
                    post.getPostLikedUsers().add(user);
                    postRepository.save(post);
                    return new ApiResponse("saved to saved posts", true, post);
                } else {
                    likedPosts.remove(post);
                    userRepository.save(user);
                }
            }
        }
        return new ApiResponse("something went wrong", false);
    }

    public ApiResponse reel(Long user_id) {
        if (userRepository.findById(user_id).isPresent()) {
            User user = userRepository.getById(user_id);

            Set<User> followings = user.getFollowing();

            List<ShowPost> showPostList = new ArrayList<>();

            for (User following : followings) {
                for (Post post : following.getPosts()) {
                    showPostList.add(makePostToShowPost(user, post));
                }
            }
            return new ApiResponse("Showing reel", true, showPostList);
        }
        return new ApiResponse("Something went wrong", false);
    }

    public ShowPost makePostToShowPost(User user, Post post) {
        ShowPost showPost = new ShowPost();
        showPost.setPost(post);
        showPost.setPostLikedByThisUser(post.getPostLikedUsers().contains(user));
        showPost.setSavedByThisUser(user.getSavedPosts().contains(post));
        return showPost;
    }

    public ApiResponse addPost(Long user_id, Long attachment_id, PostDto postDto) {

        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(attachment_id);
            if (optionalAttachment.isPresent()) {
                Attachment attachment = optionalAttachment.get();
                Post post = new Post();
                post.setDescription(postDto.getDescription());
                post.getFiles().add(attachment);
                post.setUser(user);

                Post save = postRepository.save(post);
                return new ApiResponse("post is saved", true, save);
            }
        }
        return new ApiResponse("something went wrong", false);
    }
}
