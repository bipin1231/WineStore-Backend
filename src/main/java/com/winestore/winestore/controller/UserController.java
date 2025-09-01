package com.winestore.winestore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.winestore.winestore.ApiResponse.ApiResponse;
import com.winestore.winestore.DTO.OtpRequest;
import com.winestore.winestore.DTO.UserResponseDTO;
import com.winestore.winestore.DTO.UserRequestDTO;
import com.winestore.winestore.DTO.delivery.DeliveryInfoResponseDTO;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public  ResponseEntity<ApiResponse<?>> signup(@RequestBody UserRequestDTO request) throws JsonProcessingException {
        userService.sendOtp(request);
        return ResponseEntity.ok(
                new ApiResponse<>(true,"OTP sent to your email.",null)
        );


    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<?>> verifyOtp(@RequestBody OtpRequest otpRequest) throws JsonProcessingException {
        userService.verifyOtpAndSignup(otpRequest);
        return ResponseEntity.ok(
                     new ApiResponse<>(true,"SignUp successfully.",null)
        );

    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody UserRequestDTO userDto, HttpServletResponse response) {
        UserResponseDTO user = userService.login(userDto, response);
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Logged In Successfully",user)
        );
    }


@GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUser(){
    List<UserResponseDTO> data = userService.getAllUser();
    return ResponseEntity.ok(
            new ApiResponse<>(true,"All User Data",data)
    );

}


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>>  getCurrentUser(HttpServletRequest request) {
        UserResponseDTO data= userService.getCurrentUser();
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Current User Logged in",data)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletResponse response) {
        userService.logout(response);
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Logged Out Successfully",null)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO userDto) {

        UserResponseDTO updatedUser = userService.updateUser(id, userDto);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User updated successfully", updatedUser)
        );
    }
}
