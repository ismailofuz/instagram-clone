package uz.pdp.instagramclone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.instagramclone.entity.User;
import uz.pdp.instagramclone.payload.Registration;
import uz.pdp.instagramclone.payload.ApiResponse;
import uz.pdp.instagramclone.payload.LoginDTO;
import uz.pdp.instagramclone.payload.UserDto;
import uz.pdp.instagramclone.security.JwtProvider;
import uz.pdp.instagramclone.service.AuthService;
import uz.pdp.instagramclone.service.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDTO dto){
       // String token=jwtProvider.generateToken(loginDTO.getLogin());
        ApiResponse apiResponse;
        try {
            Authentication authentication = authenticationManager.
                    authenticate(new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword()));

            User user = (User) authentication.getPrincipal();

            // kirgan odamga token berish kk
            String token = jwtProvider.generateToken(user.getUsername());

            apiResponse=  new ApiResponse("Your token : "+token,true,user);

        } catch (AuthenticationException e) {
            apiResponse =  new ApiResponse("Something went wrong",false);
        }


        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/search")
    public HttpEntity<?> search(@RequestParam String username,@RequestParam int pageNumber){

        Pageable pageable = PageRequest.of(pageNumber,10, Sort.by("username").ascending());

        ApiResponse apiResponse = authService.searchByUserName(username,pageable);

        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
    }

    /**
     * get one user from follower or not
     * @param currnt_user_id
     * @param user_profile
     * @return
     */
    @GetMapping("/{currnt_user_id}/{user_profile}")
    public HttpEntity<?> userProfile(@PathVariable Long currnt_user_id,@PathVariable Long user_profile){

       ApiResponse apiResponse =  authService.getOneUser(currnt_user_id,user_profile);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
    }

    /**
     * email va code kelsa biz uni tekshiramiz va to'g'ri bo'lsa uni webga kiritamiz
     * @param email
     * @param emailCode
     * @return
     */
    @GetMapping("/verify/email")
    public HttpEntity<?> verifyEmail(@RequestParam String email,@RequestParam String emailCode){
        ApiResponse apiResponse = authService.verifyEmail(email,emailCode);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/register")
    public HttpEntity<?> addUser(@RequestBody Registration registration){
        ApiResponse apiResponse = userService.addUser(registration);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    // follow request :/**

    /**
     * user follow qiladi agar avval follow qilmagan bo'lsa akas holsa unfollow
     * @param user_id
     * @param following_user_id
     * @return
     */
    @PostMapping("/follow/{user_id}/{following_user_id}")
    public HttpEntity<?> follow(@PathVariable Long user_id,@PathVariable Long following_user_id){
        ApiResponse apiResponse = authService.follow(user_id,following_user_id);

        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    /**
     * edit user profile settings name,username ...
     * @param dto
     * @return
     */
    // edit user :
    @PutMapping("/edit")
    public HttpEntity<?> edit(@Valid @RequestBody UserDto dto){
        ApiResponse apiResponse = authService.editProfile(dto);

        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

//    editing profilePhoto
    @PatchMapping("/editProfilePhoto/{attachment_id}")
    public HttpEntity<?> editProfilePhoto(@PathVariable Long attachment_id){
        ApiResponse apiResponse = authService.editProfilePhoto(attachment_id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
