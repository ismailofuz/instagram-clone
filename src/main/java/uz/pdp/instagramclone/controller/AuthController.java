package uz.pdp.instagramclone.controller;

//import com.example.soliqjwttask.dto.LoginDTO;
//import com.example.soliqjwttask.security.JwtProvider;
//import com.example.soliqjwttask.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import uz.pdp.instagramclone.payload.LoginDTO;
import uz.pdp.instagramclone.security.JwtProvider;
import uz.pdp.instagramclone.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthService authService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDTO loginDTO){
        String token=jwtProvider.generateToken(loginDTO.getUserName());
        return ResponseEntity.ok().body(token);
    }
}
