package uz.pdp.instagramclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import uz.pdp.instagramclone.config.MailSender;
import uz.pdp.instagramclone.payload.Registration;
import uz.pdp.instagramclone.entity.User;
import uz.pdp.instagramclone.payload.ApiResponse;
import uz.pdp.instagramclone.repository.UserRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final MailSender mailSender;


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
            // user.setEnabled(false);

            userRepository.save(user);

            try {
            SimpleMailMessage message = new SimpleMailMessage();
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.addHeader("content-type", "html/text");
            message.setFrom("pdp@gmail.com");

            message.setTo(registration.getRegister());

                message.setSubject("Confirmation code");
                message.setText(emailCode);
                message.setSentDate(new Date());
                mailSender.getEmail().send(message);

                return new ApiResponse("check your email code is sent",true);

            } catch (MessagingException e) {
                e.printStackTrace();
            }


            // code for sending message for email===============
        }

        return new ApiResponse("Something went wrong!", false);
    }


}
