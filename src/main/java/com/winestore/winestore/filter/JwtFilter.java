package com.winestore.winestore.filter;

import com.winestore.winestore.repository.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.winestore.winestore.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String jwt = null;
        String username = null;
        String provider = null;

        // 1. First try Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }

        // 2. If not found, try cookie
        if (jwt == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                }
            }
        }

        // 3. If we found a token and no authentication exists yet
        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                var claims = jwtUtil.extractAllClaims(jwt);
                username = claims.get("email", String.class);
                provider = claims.get("authProvider", String.class);

                if (jwtUtil.validateToken(jwt)) {
                    UserDetails userDetails = getUserDetails(username, provider);
                    if (userDetails != null) {
                        setAuthentication(userDetails, request);
                    }
                }
            } catch (Exception e) {
                // Invalid JWT → let it pass, Spring Security will handle unauthorized
                logger.warn("JWT validation failed: {}", e);
            }
        }

        chain.doFilter(request, response);
    }


    private UserDetails getUserDetails(String email, String provider) {
        if ("none".equals(provider)) {
            // Normal login
            return userDetailsService.loadUserByUsername(email);
        } else {
            // OAuth login
            return userRepo.findByEmailAndAuthProvider(email, provider)
                    .map(user -> org.springframework.security.core.userdetails.User.builder()
                            .username(user.getEmail())
                            .password("")
                            .roles(user.getRoles())
                            .build())
                    .orElse(null);
        }
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
