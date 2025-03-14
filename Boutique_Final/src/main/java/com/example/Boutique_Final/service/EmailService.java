package com.example.Boutique_Final.service;

import com.example.Boutique_Final.model.Order;
import com.example.Boutique_Final.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    // Fix: Use ${} to fetch the property value correctly
    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    // Sends order confirmation email
    public void sendOrderConfirmation(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(order.getUser().getEmail());
        message.setSubject("Order Confirmation");
        message.setText("Your order has been confirmed. Order ID: " + order.getId());
        mailSender.send(message);
    }

    // Sends email confirmation code
    public void sendConfirmationCode(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Confirm Your Email");
        message.setText("Please confirm your email by entering this code: " + user.getConfirmationCode());
        mailSender.send(message);
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // Use true if sending HTML content

            mailSender.send(message);

            System.out.println("Email sent successfully to: " + to);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public void sendPasswordResetEmail(User user, String resetToken) {
        String subject = "Password Reset Request";
        String resetUrl = "http://localhost:3000/reset-password?token=" + resetToken; // Adjust frontend URL accordingly
        String body = "Hi " + user.getEmail() + ",\n\nClick the link below to reset your password:\n" + resetUrl + "\n\nIf you didn't request a password reset, please ignore this email.";

        sendEmail(user.getEmail(), subject, body);
    }

}
