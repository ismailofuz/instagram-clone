package uz.pdp.instagramclone.controller;

//import com.example.soliqjwttask.dto.LoginDTO;
//import com.example.soliqjwttask.security.JwtProvider;
//import com.example.soliqjwttask.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import uz.pdp.instagramclone.entity.Registration;
import uz.pdp.instagramclone.payload.ApiResponse;
import uz.pdp.instagramclone.payload.LoginDTO;
import uz.pdp.instagramclone.security.JwtProvider;
import uz.pdp.instagramclone.service.AuthService;
import uz.pdp.instagramclone.service.UserService;

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
    public HttpEntity<?> login(@RequestBody LoginDTO loginDTO){
       // String token=jwtProvider.generateToken(loginDTO.getLogin());
        ApiResponse apiResponse = authService.login(loginDTO);

        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
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


}
