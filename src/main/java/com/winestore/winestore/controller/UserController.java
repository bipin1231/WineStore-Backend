package com.winestore.winestore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.winestore.winestore.DTO.LoginResponseDTO;
import com.winestore.winestore.DTO.OtpRequest;
import com.winestore.winestore.DTO.UserResponseDTO;
import com.winestore.winestore.DTO.UserRequestDTO;
import com.winestore.winestore.entity.User;
import com.winestore.winestore.service.RedisService;
import com.winestore.winestore.service.UserDetailServiceImpl;
import com.winestore.winestore.service.UserService;
import com.winestore.winestore.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private AuthenticationManager authenticationManager;

@Autowired
    private UserService userService;

@Autowired
private RedisService redisService;


@Autowired
private JavaMailSender javaMailSender;



    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody UserRequestDTO request)throws JsonProcessingException {
        // Generate 4-digit OTP
        String otp = String.valueOf(1000 + new Random().nextInt(9000));

        // Create and send OTP email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bipinadhikari234@gmail.com"); // Make sure this is a valid sender email
        message.setTo(request.getEmail());
        message.setSubject("Your OTP Code: " + otp);
        message.setText("Hello,\n\nYour OTP for verification is: " + otp + "\n\nThanks,\nWine Store Team");

        javaMailSender.send(message);

        // Store OTP and request data (email, etc.) in Redis for later verification
        redisService.storeOtpAndData(request, otp);

        return ResponseEntity.ok(Map.of("message", "OTP sent to your email."));
    }



    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody OtpRequest otpRequest) throws JsonProcessingException{

        String storedOtp = redisService.getOtp(otpRequest.getEmail());

        if (storedOtp == null) return ResponseEntity.ok(Map.of("message", "OTP expired."));
        if (!storedOtp.equals(otpRequest.getOtp())) return ResponseEntity.ok(Map.of("message", "Invalid OTP."));

        UserRequestDTO signupData = redisService.getSignupData(otpRequest.getEmail());
        if (signupData == null) ResponseEntity.ok(Map.of("message", "SignUp Session Expired."));
        User user = new User();
        user.setEmail(signupData.getEmail());
        user.setPassword(signupData.getPassword()); // hash in real app
        user.setAuthProvider("none");

      userService.addUser(user);

        redisService.deleteData(otpRequest.getEmail());

        return ResponseEntity.ok(Map.of("message", "SignUp successfully."));


    }




    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO userDto, HttpServletResponse response) {
        try {
            // 1. Authenticate
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword())
            );

            // 2. Find user
            User user = userService.findByEmailAndAuthProvider(userDto.getEmail(), "none")
                    .orElseThrow(() -> new IllegalArgumentException("User not Found"));

            // 3. Load user details & generate token
            UserDetails userDetails = userDetailService.loadUserByUsername(user.getEmail());
            String jwt = jwtUtil.generateToken(user);

            // 4. Create HttpOnly cookie
            ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                    .httpOnly(true)          // not accessible via JS
                    //.secure(true)            // only over HTTPS (use false for local dev if needed)
                    .path("/")               // available for all endpoints
                    .maxAge(72 * 60 * 60)    // 1 day
                  //  .sameSite("Strict")      // CSRF protection
                    .build();

            // 5. Add cookie to response
            response.addHeader("Set-Cookie", cookie.toString());

            // 6. Return user info (without JWT in body anymore)
            return ResponseEntity.ok(new UserResponseDTO(user));

        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }


@GetMapping
    public List<UserResponseDTO> getUser(){
    return userService.getUser();
}


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        // JwtFilter already sets SecurityContext
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            User user = userService.findByEmailAndAuthProvider(userDetails.getUsername(), "none")
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            return ResponseEntity.ok(new UserResponseDTO(user));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // expire immediately
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }





}
