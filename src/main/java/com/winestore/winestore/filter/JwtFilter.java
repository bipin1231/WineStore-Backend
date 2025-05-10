package com.winestore.winestore.filter;


import com.winestore.winestore.repository.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
public class JwtFilter  extends OncePerRequestFilter{
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    //    String authorizationHeader = request.getHeader("Authorization");
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String provider=null;
        String jwt = null;
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//
//            username = jwtUtil.extractAllClaims(jwt).get("email", String.class);
//            provider=jwtUtil.extractAllClaims(jwt).get("authProvider", String.class);
//        }
//        if (username != null) {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//            if (jwtUtil.validateToken(jwt)) {
//                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        }


        if (authHeader != null && authHeader.startsWith("Bearer ")) {
             jwt = authHeader.substring(7); // Skip "Bearer "

            if (jwtUtil.validateToken(jwt)) {
                username = jwtUtil.extractAllClaims(jwt).get("email", String.class);
           provider=jwtUtil.extractAllClaims(jwt).get("authProvider", String.class);

                UserDetails userDetails = getUserDetails(username, provider);
                if (userDetails != null) {
                    setAuthentication(userDetails, request);
                }
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