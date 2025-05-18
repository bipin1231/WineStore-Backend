package com.winestore.winestore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.winestore.winestore.DTO.OtpRequest;
import com.winestore.winestore.DTO.UserDTO;
import com.winestore.winestore.DTO.UserRequestDTO;
import com.winestore.winestore.entity.User;
import com.winestore.winestore.service.RedisService;
import com.winestore.winestore.service.UserDetailServiceImpl;
import com.winestore.winestore.service.UserService;
import com.winestore.winestore.tempStore.OtpStore;
import com.winestore.winestore.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

@GetMapping("/send-email")
public String sendEmail(){
    try{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bipinadhikari234.com");
        message.setTo("moviespedia234@gmail.com");
        message.setSubject("Hey whats up");
        message.setText("thats awesome");

        javaMailSender.send(message);
    return "success";
    }catch (Exception e){
        return e.getMessage();

    }

}

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
public ResponseEntity<?> login(@RequestBody UserRequestDTO userDto){
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(),userDto.getPassword())
        );
        User user=userService.findByEmailAndAuthProvider(userDto.getEmail(),"none").orElseThrow(()->new IllegalArgumentException("User not Found"));

        UserDetails userDetails=userDetailService.loadUserByUsername(user.getEmail());
        String jwt=jwtUtil.generateToken(user);
        System.out.println("jwt token is---"+jwt);
        return ResponseEntity.ok(Map.of("token", jwt));
    }catch (Exception e){
        return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
    }
}

@GetMapping
    public List<UserDTO> getUser(){
    return userService.getUser();
}

//@GetMapping("{name}")
//    public Optional<Product> getProductByName(@PathVariable String name){
//    return categoryService.getProductByName(name);
//}



}
