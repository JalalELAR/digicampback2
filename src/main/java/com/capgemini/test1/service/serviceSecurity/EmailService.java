package com.capgemini.test1.service.serviceSecurity;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    String mailAdmin="mouad-ali.alil@capgemini.com";

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setPort(587);
            mailSender.setHost("MTACOR1.capgemini.com");
            mailSender.setProtocol("smtp");
            mailSender.setUsername("mouad-ali.alil@capgemini.com");
            mailSender.setPassword("Support2424");

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MULTIPART_MODE_MIXED,
                    UTF_8.name()
            );
            helper.setFrom(mailAdmin);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.addBcc(mailAdmin);

            helper.setText(text);
            mailSender.send(mimeMessage);

            /*SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);*/
            System.out.println("Email envoyé avec succès à " + to);
        } catch (MailAuthenticationException e) {
            System.out.println("EmailService::sendFeedbackRequestEmail wrong password");
            throw new BadCredentialsException("password.wrong");
        } catch (MailSendException | MessagingException e) {
            System.out.println("EmailService::sendFeedbackRequestEmail failed to sent email");
            throw new IllegalStateException("exception.messaging");
        } catch (Exception e) {
            System.out.println("EmailService::sendFeedbackRequestEmail unknown error");
            throw new IllegalStateException("exception.messaging");
        }
    }


}
