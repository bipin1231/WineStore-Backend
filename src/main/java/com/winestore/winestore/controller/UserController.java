package com.winestore.winestore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.winestore.winestore.DTO.UserDTO;
import com.winestore.winestore.DTO.UserRequestDTO;
import com.winestore.winestore.entity.Category;
import com.winestore.winestore.entity.User;
import com.winestore.winestore.service.CategoryService;
import com.winestore.winestore.service.RedisService;
import com.winestore.winestore.service.UserDetailServiceImpl;
import com.winestore.winestore.service.UserService;
import com.winestore.winestore.tempStore.OtpStore;
import com.winestore.winestore.tempStore.TempUserDataStore;
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
    public String saveUser(@RequestBody UserRequestDTO request) throws JsonProcessingException {

    Random random=new Random();
    String otp= String.valueOf(1000+random.nextInt(9000));

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("bipinadhikari234.com");
    message.setTo("moviespedia234@gmail.com");
    message.setSubject("enter this otp  "+otp);
    message.setText("thats awesome");

    javaMailSender.send(message);

    redisService.storeOtpAndData(request, otp);
  //  emailService.sendOtp(request.getEmail(), otp);


//   OtpStore.storeOtp(userDto.getEmail(),otp);
//    TempUserDataStore.save(userDto.getEmail(),userDto.getPassword());


//    User user=new User();

//    user.setEmail(userDto.getEmail());
//    user.setPassword(userDto.getPassword());
//    user.setAuthProvider("none");
//    userService.addUser(user);
    return "enter the otp";
}


    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp){
    System.out.println(OtpStore.getOtp(email));

      if(otp.equals(OtpStore.getOtp(email))) {
       return TempUserDataStore.getPassword(email);
      }else{
          return "error in veridcation";
      }


    }




@PostMapping("/login")
public ResponseEntity<String> login(@RequestBody UserRequestDTO userDto){
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(),userDto.getPassword())
        );
        User user=userService.findByEmailAndAuthProvider(userDto.getEmail(),"none").orElseThrow(()->new IllegalArgumentException("User not Found"));

        UserDetails userDetails=userDetailService.loadUserByUsername(user.getEmail());
        String jwt=jwtUtil.generateToken(user);
        System.out.println("jwt token is---"+jwt);
        return new ResponseEntity<>(jwt, HttpStatus.OK);
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
