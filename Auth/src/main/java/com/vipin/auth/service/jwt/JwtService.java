package com.vipin.auth.service.jwt;

import com.vipin.auth.exceptions.JwtGenerationException;
import com.vipin.auth.model.entity.User;
import com.vipin.auth.repository.UserRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtService {
    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${jwt_secret}")
    private String jwtSecret ;
    private Long jwtExpiration= 30 * 24 * 60 * 60 * 1000L;

    public String generateToken(String email) throws Exception {

        Date currentDate = new Date();
        try {
            User user = userRepository.findByEmail(email);
            Date expirationTime = new Date(currentDate.getTime() + jwtExpiration);
            return Jwts.builder()
                    .setSubject(user.getFullName())
                    .claim("role", user.getRole())
                    .claim("userId",user.getUserId())
                    .claim("email",user.getEmail())
                    .setIssuedAt(currentDate)
                    .setExpiration(expirationTime)
                    .signWith(SignatureAlgorithm.HS256, jwtSecret)
                    .compact();
        }
        catch (Exception e){
            log.error("Something went wrong while generating token : {}",e.getMessage());
            throw new JwtGenerationException(e.getMessage());
        }
    }
    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return  claims.getSubject();
    }
    public boolean validateToken(String token){
        log.info("Validating token ....");
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            log.info("VALIDATED");
            return true;
        }
        catch (ExpiredJwtException e){
            log.error("TOKEN EXPIRED");
            throw new ExpiredJwtException(e.getHeader(),e.getClaims(),"Jwt token is expired");
        }
        catch (SignatureException e){
            log.error("Invalid Token");
            throw new JwtException("Jwt token is invalid");
        }
        catch (Exception e){
            log.error("Something went wrong");
            throw new RuntimeException("Something went wrong with the jwt token validation");
        }

    }
    public void expireToken(String token){
        Date currentDate = new Date();
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        claims.setExpiration(new Date(currentDate.getTime()));
    }
    public String getEmailFromRequest(HttpServletRequest request){
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("email", String.class);
        } catch (SignatureException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getJWTFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}