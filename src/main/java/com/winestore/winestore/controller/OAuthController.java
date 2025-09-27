package com.winestore.winestore.controller;

import com.winestore.winestore.entity.User;
import com.winestore.winestore.oauth.GoogleOAuthService;
import com.winestore.winestore.repository.UserRepo;
import com.winestore.winestore.service.UserService;
import com.winestore.winestore.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class OAuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final GoogleOAuthService googleOAuthService;
    @Autowired
    private UserRepo userRepo;

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    public OAuthController(JwtUtil jwtUtil,
                           UserService userService,
                           GoogleOAuthService googleOAuthService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.googleOAuthService = googleOAuthService;
    }

    // Step 1: Redirect user to Google login page
    @GetMapping("/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String oauthUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=email%20profile";

        response.sendRedirect(oauthUrl);
    }

    // Step 2: Handle Google's callback
    @GetMapping("/google/callback")
    public void handleGoogleCallback(@RequestParam String code,
                                     HttpServletResponse response) throws IOException {
        // 1. Exchange code for access token
        String accessToken = googleOAuthService.getAccessToken(code);

        // 2. Fetch user info from Google
        Map<String, Object> attributes = googleOAuthService.getUserInfo(accessToken);
        String email = (String) attributes.get("email");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        String imageUrl = (String) attributes.get("picture");
        String provider="google";

        // Find or create user
        Optional<User> existingUser = userRepo.findByEmailAndAuthProvider(email, provider);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            //  Update info if it changed
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setImageUrl(imageUrl);

        } else {
            //  Create new user
            user = User.builder()
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .imageUrl(imageUrl)
                    .authProvider(provider)
                    .roles("USER")
                    .build();
        }
        userRepo.save(user);



        // 4. Generate JWT
        String jwt = jwtUtil.generateToken(user);

        // 5. Set JWT as HttpOnly cookie (first-party)
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // or None if cross-site
                .path("/")
                .maxAge(72 * 60 * 60) // 3 days
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        // 6. Redirect to frontend
        response.sendRedirect(frontendUrl + "/login-success");
    }
}
