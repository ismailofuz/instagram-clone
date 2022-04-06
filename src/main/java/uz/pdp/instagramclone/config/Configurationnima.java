package uz.pdp.instagramclone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

@Configuration
public class Configurationnima {


    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smpt.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("utkirjoraqulov68@gmail.com"); // gmail
        mailSender.setPassword("utkir0706"); // passsword
        Properties properties = mailSender.getJavaMailProperties();

        properties.put("mail.transport.protocol","smpt");
        properties.put("mail.smpt.auth","true");
        properties.put("mail.starttls.enable","true");
        properties.put("mail.debug","true");

        return mailSender;
    }
}
