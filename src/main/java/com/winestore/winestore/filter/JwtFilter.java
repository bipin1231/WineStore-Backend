package com.winestore.winestore.filter;

import com.winestore.winestore.entity.User;
import com.winestore.winestore.repository.UserRepo;
import com.winestore.winestore.service.CustomOAuth2UserDetails;
import com.winestore.winestore.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
    // Skip JWT auth for preflight but still continue the chain
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
        chain.doFilter(request, response);
        return;
    }


        String token = extractToken(request);
        logger.info("Found JWT: {}"+token);
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                var claims = jwtUtil.extractAllClaims(token);

                String email = claims.get("email", String.class);
                String provider = claims.get("authProvider", String.class);

                if (jwtUtil.validateToken(token)) {
                    logger.info("token is valid");
                    UserDetails userDetails = getUserDetails(email, provider);

                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception e) {
                logger.warn("JWT validation failed: {}", e);
            }
        }

        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        // Check Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // Check cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
    private UserDetails getUserDetails(String email, String provider) {
        Optional<User> userOpt = userRepo.findByEmailAndAuthProvider(email, provider);

        return userOpt.map(user -> new CustomOAuth2UserDetails(user, null))
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

//    private UserDetails getUserDetails(String email, String provider) {
//        // OAuth login
//        if (!"none".equals(provider)) {
//            Optional<User> userOpt = userRepo.findByEmailAndAuthProvider(email, provider);
//            return userOpt.map(user -> new CustomOAuth2UserDetails(user, null)).orElse(null);
//        }
//
//        // Normal login (admin)
//        return userDetailsService.loadUserByUsername(email);
//    }
}
