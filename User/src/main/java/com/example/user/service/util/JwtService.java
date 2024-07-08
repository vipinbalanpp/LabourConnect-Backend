package com.example.user.service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {
    private final String jwtSecret = "2D4A614E645267556B58703273357638792F423F4428472B4B6250655368566D";
    private final Long jwtExpiration = 30 * 24 * 60 * 60 * 1000L;

    public String getRoleFromRequest(HttpServletRequest request) {
       String token = getTokenFromRequest(request);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("role", String.class);
        } catch (SignatureException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTokenFromRequest(HttpServletRequest request) {
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
        return token;
    }

    public String getEmailFromToken(String token) {
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
}
