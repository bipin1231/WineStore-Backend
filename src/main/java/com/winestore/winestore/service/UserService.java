package com.winestore.winestore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.winestore.winestore.DTO.OtpRequest;
import com.winestore.winestore.DTO.UserRequestDTO;
import com.winestore.winestore.DTO.UserResponseDTO;
import com.winestore.winestore.entity.User;
import com.winestore.winestore.repository.UserRepo;
import com.winestore.winestore.service.email.BrevoEmailService;
import com.winestore.winestore.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RedisService redisService;
    @Autowired
    private BrevoEmailService brevoEmailService;


    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private AuthenticationManager authenticationManager;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // ---------------------- SIGNUP ----------------------
    public void sendOtp(UserRequestDTO request) throws JsonProcessingException {
        Optional<User> user=userRepo.findByEmailAndAuthProvider(request.getEmail(),"none");
        if(user.isPresent()){
            throw new RuntimeException("This Account Already Exits");
        }
        // Generate 4-digit OTP
        String otp = String.valueOf(1000 + new Random().nextInt(9000));

        // Create and send OTP email
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("bipinadhikari234@gmail.com");
//        message.setTo(request.getEmail());
//        message.setSubject("Your OTP Code: " + otp);
//        message.setText("Hello,\n\nYour OTP for verification is: " + otp + "\n\nThanks,\nWine Store Team");
//        javaMailSender.send(message);
        brevoEmailService.sendOtpEmail(request.getEmail(),otp);

        // Store OTP and request data (email, etc.) in Redis for later verification
        redisService.storeOtpAndData(request, otp);
    }

    public void verifyOtpAndSignup(OtpRequest otpRequest) throws JsonProcessingException {
        String storedOtp = redisService.getOtp(otpRequest.getEmail());
        if (storedOtp == null) throw new RuntimeException("OTP expired");
        if (!storedOtp.equals(otpRequest.getOtp())) throw new RuntimeException("Invalid OTP");

        UserRequestDTO signupData = redisService.getSignupData(otpRequest.getEmail());
        if (signupData == null) throw new RuntimeException("SignUp session expired");
        ;
        User user = new User();
        user.setEmail(signupData.getEmail());
        user.setPassword(signupData.getPassword()); // hash in real app
        user.setFirstName(signupData.getFirstName());
        user.setLastName(signupData.getLastName());
        user.setAuthProvider("none");

        addUser(user);

        redisService.deleteData(otpRequest.getEmail());

    }


    public void addUser(User user) {
        userRepo.findByEmailAndAuthProvider(user.getEmail(), user.getAuthProvider())
                .ifPresent(u -> {
                    throw new RuntimeException("User already exists with this email and authProvider");
                });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
    // ---------------------- LOGIN ----------------------

    public UserResponseDTO login(UserRequestDTO userDto, HttpServletResponse response) {
        // 1. Authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword())
        );

        // 2. Find user
        User user = findByEmailAndAuthProvider(userDto.getEmail(), "none");

        // 3. Load user details & generate token
        UserDetails userDetails = userDetailService.loadUserByUsername(user.getEmail());
        String jwt = jwtUtil.generateToken(user);

        // 4. Create HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(true) // ✅ needed in production with HTTPS
                .sameSite("None") // ✅ allow cross-site cookie (frontend <-> backend)
                .path("/")
                .maxAge(72 * 60 * 60)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return new UserResponseDTO(user);
    }

    // ---------------------- LOGOUT ----------------------
    public void logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)           // ✅ needed for HTTPS
                .sameSite("None")       // ✅ allow cross-site
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

    }

    // ---------------------- FIND USER ----------------------
    public User findByEmailAndAuthProvider(String email, String authProvider) {
        return userRepo.findByEmailAndAuthProvider(email, authProvider)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }


    public List<UserResponseDTO> getAllUser() {
        return userRepo.findAll().stream().map(UserResponseDTO::new).collect(Collectors.toList());
    }


    public User getUserInfoById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    // ---------------------- CURRENT USER ----------------------
    public UserResponseDTO getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomOAuth2UserDetails userDetails) {
            return new UserResponseDTO(userDetails.getUser());
        }

        throw new RuntimeException("Not logged in");
    }





    // ---------------------- UPDATE USER ----------------------
    public UserResponseDTO updateUser(Long userId, UserRequestDTO userDto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Update only non-null fields (so partial update works)
        if (userDto.getFirstName() != null) user.setFirstName(userDto.getFirstName());
        if (userDto.getLastName() != null) user.setLastName(userDto.getLastName());


        User updatedUser = userRepo.save(user);
        return new UserResponseDTO(updatedUser);
    }
}

