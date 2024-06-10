package com.vipin.notification.controller;
import com.vipin.notification.dto.VerifyOtpResponseDto;
import com.vipin.notification.service.MailService;
import com.vipin.notification.service.OtpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1")
public class NotificationController {
    OtpService otpService;
    MailService mailService;
    public NotificationController(OtpService otpService, MailService mailService){
        this.otpService = otpService;
        this.mailService = mailService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email){


           System.out.println(email+" email from frontent");
           String otp =   otpService.generateAndStoreOtp(email);
           System.out.println(otp+" otosfhls");
           mailService.sendOtpEmail(email,otp);
           return new ResponseEntity<>("otp send successfully", HttpStatus.OK);

    }
    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOtpResponseDto> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(email, otp);
        System.out.println("verifyOTp :"+otp);
       try {
           if (isValid) {

               return new ResponseEntity<>(new VerifyOtpResponseDto(true,"Otp verified successfully"),HttpStatus.OK);
           } else {
               return new ResponseEntity<>(new VerifyOtpResponseDto(false,"Otp verification failed"),HttpStatus.OK);
           }
       }catch (Exception e){
           throw new RuntimeException();
       }
    }

}
