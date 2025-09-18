package com.winestore.winestore.service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

import java.util.List;

@Service
public class BrevoEmailService {

    @Value("${BREVO.API_KEY}")
    private String apiKey;

    @Value("${BREVO.SENDER_EMAIL}")
    private String senderEmail;

    @Value("${BREVO.SENDER_NAME:Wine Store}")
    private String senderName;

    public void sendOtpEmail(String toEmail, String otp) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();

        // Set API key
        ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKeyAuth.setApiKey(apiKey);

        TransactionalEmailsApi emailsApi = new TransactionalEmailsApi();

        SendSmtpEmail email = new SendSmtpEmail()
                .sender(new SendSmtpEmailSender().email(senderEmail).name(senderName))
                .to(List.of(new SendSmtpEmailTo().email(toEmail)))
                .subject("Your OTP Code: " + otp)
                .textContent("Hello,\n\nYour OTP for verification is: " + otp + "\n\nThanks,\nWine Store Team");

        try {
            emailsApi.sendTransacEmail(email);
        } catch (ApiException e) {
            throw new RuntimeException("Failed to send OTP via Brevo", e);
        }
    }
}
