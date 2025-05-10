package com.winestore.winestore.tempStore;
import java.util.Map;
import java.util.concurrent.*;

public class OtpStore {
    // Map<email, OTP info>
    private static final Map<String, String> otpMap = new ConcurrentHashMap<>();
    private static final long EXPIRY_DURATION_MS = 5 * 60 * 1000; // 5 minutes

    public static void storeOtp(String email, String otp) {

        otpMap.put(email, otp);

    }
    public static String getOtp(String email) {
        return otpMap.get(email);
    }






}
