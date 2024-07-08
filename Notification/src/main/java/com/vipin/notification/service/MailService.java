package com.vipin.notification.service;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

   private final JavaMailSender javaMailSender;
    public void sendOtpEmail(String recipientEmail, String otp) {
        try {

            SimpleMailMessage message=new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Your OTP Code");
            message.setText("Your otp from LabourConnect app is:  " + otp);
            javaMailSender.send(message);
            System.out.println("Mail send successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
