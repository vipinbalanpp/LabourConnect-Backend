package com.example.booking.service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {
    private final String jwtSecret = "2D4A614E645267556B58703273357638792F423F4428472B4B6250655368566D";
    private final Long jwtExpiration = 30 * 24 * 60 * 60 * 1000L;

    public String getIdFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("userId", String.class);
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
    public Long getUserIdFromRequest(HttpServletRequest request){
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
            return claims.get("userId", Long.class);
        } catch (SignatureException e) {
            e.printStackTrace();
            return null;
        }
    }
}