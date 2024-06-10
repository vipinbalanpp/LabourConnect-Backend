package com.vipin.notification.service;

import com.vipin.notification.exception.InvalidEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Random;

@Service
@Slf4j
public class OtpService {
    private RedisTemplate<String,String> redisTemplate;
     private RestTemplate restTemplate;
    public OtpService(RedisTemplate<String,String> redisTemplate,
                      RestTemplate restTemplate){
        this.redisTemplate = redisTemplate;
        this.restTemplate = restTemplate;
    }

    public String generateAndStoreOtp(String email){
        System.out.println("here");
        if(restTemplate.getForObject("http://localhost:8081/auth/api/v1/emailExists/"+email,Boolean.class)){
            throw new InvalidEmailException("Email id exists");
        }
        Random random=new Random();
        int otp=1000+random.nextInt(9999);
        System.out.println(otp);
        String key = email;
        redisTemplate.opsForValue().set(key, String.valueOf(otp));
        redisTemplate.expire(email, Duration.ofMinutes(2));
        return String.valueOf(otp);
    }

    public boolean verifyOtp(String email, String otp) {
        try {
            if (!redisTemplate.hasKey(email)) {
                return false;
            }
            String storedOtp = redisTemplate.opsForValue().get(email);
            System.out.println(email);
            System.out.println(storedOtp);
            if (storedOtp == null) {
                log.info("No otp found in redis for email:{}", email);
                return false;
            }
            if (otp.equals(storedOtp)) {
                redisTemplate.delete(email);
                System.out.println("Otp verified successfully");
                return true;
            }
        } catch (Exception e) {
            log.info("Error verifying OTP from Redis for email:{}", email);
            throw new RuntimeException("Otp verification failed");
        }
        return false;
    }
}
