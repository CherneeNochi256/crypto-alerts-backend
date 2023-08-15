package ru.maxim.cryptoalertbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.maxim.cryptoalertbackend.dto.ApiResponse;
import ru.maxim.cryptoalertbackend.entities.CoinAlert;
import ru.maxim.cryptoalertbackend.entities.Confirmation;
import ru.maxim.cryptoalertbackend.entities.User;
import ru.maxim.cryptoalertbackend.exception.ResourceNotFoundException;
import ru.maxim.cryptoalertbackend.repository.ConfirmationRepository;
import ru.maxim.cryptoalertbackend.repository.UserRepository;

import static ru.maxim.cryptoalertbackend.utills.EmailUtils.getCoinAlertEmailMessage;
import static ru.maxim.cryptoalertbackend.utills.EmailUtils.getConfirmationEmailMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    public static final String EMAIL_VERIFICATION = "Email Verification";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "emailtemplate";
    public static final String TEXT_HTML_ENCONDING = "text/html";


    private final JavaMailSender emailSender;
    private final ConfirmationRepository confirmationRepository;
    private final UserRepository userRepository;
//    private final TemplateEngine templateEngine;



    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendConfirmEmailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(EMAIL_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getConfirmationEmailMessage(name, host, token));
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Async
    public void sendCoinAlertMessage(String name, String to, CoinAlert coinAlert){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(EMAIL_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getCoinAlertEmailMessage(name, coinAlert));
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public ApiResponse verifyEmailByToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Confirmation entity with token" + token + "not found"));

        User user = confirmation.getUser();
        user.setEmailConfirmed(true);
        userRepository.save(user);
        //confirmationRepository.delete(confirmation);
        return new ApiResponse(Boolean.TRUE,"Email successfully confirmed!");
    }

}
