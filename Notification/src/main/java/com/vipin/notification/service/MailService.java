package com.vipin.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class MailService {
    private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @Autowired
    JavaMailSender javaMailSender;



    public void sendOtpEmail(String recipientEmail, String otp) {
        try {

            SimpleMailMessage message=new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Your OTP Code");
            message.setText("Your otp from LabourConnect app is:  "
                    + otp);
            javaMailSender.send(message);
            System.out.println("Mail send successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void storeOTP(String email, String otp, int ttlMinutes) {

    }
    public void resendOTPEmail(String recipientEmail, String otp) {
        try {

            SimpleMailMessage message=new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Your OTP Code");
            message.setText("Your new otp from to shoose app is:  "
                    + otp);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
