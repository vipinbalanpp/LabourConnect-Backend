//package com.vipin.auth.filter;
//
//import com.vipin.auth.service.jwt.JwtService;
//import io.jsonwebtoken.ExpiredJwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.security.SignatureException;
//
//@Component
//@Slf4j
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtService jwtService;
//    @Autowired
//    private UserDetailsService userDetailsService;
//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain) throws ServletException, IOException {
////        String token = jwtService.getJWTFromRequest(request);
//        try {
//            if (StringUtils.hasText(token)
//                    && jwtService.validateToken(token)) {
//                String username = jwtService.getUsernameFromToken(token);
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
////                request.setAttribute("userId",jwtService.getUserIdFromRequest(request));
//            }
//        } catch (ExpiredJwtException e) {
//            log.error("Expired jwt");
//            setUnauthorizedResponse(response, "Expired JWT");
//        } catch (Exception e) {
//            log.error("Something went wrong while parsing the token {}", e.getMessage());
//            setUnauthorizedResponse(response, "Error parsing JWT token");
//        }
//        filterChain.doFilter(request,response);
//
//    }
//
//    private void setUnauthorizedResponse(HttpServletResponse response, String errorMessage) throws IOException {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setContentType("application/json");
//        response.getWriter().write("{\"error\":\"" + errorMessage + "\"}");
//        response.getWriter().flush();
//    }
//
//}
