package com.winestore.winestore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winestore.winestore.DTO.UserRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    public void storeOtpAndData(UserRequestDTO request, String otp) throws JsonProcessingException {
        try {
            String userJson = mapper.writeValueAsString(request);
            redisTemplate.opsForValue().set("OTP:" + request.getEmail(), otp, 5, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set("SIGNUP:" + request.getEmail(), userJson, 5, TimeUnit.MINUTES);
            System.out.println("Stored OTP and data in Redis.");
        } catch (Exception e) {
            System.err.println("Redis store failed: " + e.getMessage());
        }
    }
    public String getOtp(String email) {
        return redisTemplate.opsForValue().get("OTP:" + email);
    }

    public UserRequestDTO getSignupData(String email) throws JsonProcessingException {
        String json = redisTemplate.opsForValue().get("SIGNUP:" + email);
        return json != null ? mapper.readValue(json, UserRequestDTO.class) : null;
    }

    public void deleteData(String email) {
        redisTemplate.delete("OTP:" + email);
        redisTemplate.delete("SIGNUP:" + email);
    }
}
