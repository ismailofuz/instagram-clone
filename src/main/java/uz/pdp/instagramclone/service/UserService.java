package uz.pdp.instagramclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.instagramclone.entity.Registration;
import uz.pdp.instagramclone.entity.User;
import uz.pdp.instagramclone.payload.ApiResponse;
import uz.pdp.instagramclone.repository.UserRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public ApiResponse addUser(Registration registration) {
        if (userRepository.existsByUsernameIgnoreCase(registration.getUsername())) {
            return new ApiResponse("This username isn't available. Please try another.", false);
        }
        if (userRepository.existsByEmailIgnoreCase(registration.getRegister())) {
            return new ApiResponse("This email isn't available. Please try another.", true);
        }
        User user = new User();

        boolean contains = registration.getRegister().contains("@");
        if (contains) {

            user.setEmail(registration.getRegister());
            user.setName(registration.getFullName());
            user.setUsername(registration.getUsername());
            user.setPassword(registration.getPassword());

            // real email verification :
            String emailCode = String.valueOf(UUID.randomUUID());

            user.setEmailCode(emailCode);
            user.setEnabled(false);

            boolean sendEmail = sendEmail(registration.getRegister(), "" + emailCode);

            if (sendEmail){
                return new ApiResponse("Please verify email by sending (nonereply@gmail.com)",true);
            }
        }

        return new ApiResponse("Something went wrong!", false);
    }

    public boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom("nonereply@gmail.com"); // bu muhim emas shunchaki ko'rinish
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Confirmation code");
            mailMessage.setText("Enter the verification code :" + emailCode);


            mailSender.send(mailMessage);
            return true;
        } catch (MailException e) {
            return false;
        }
    }
}
