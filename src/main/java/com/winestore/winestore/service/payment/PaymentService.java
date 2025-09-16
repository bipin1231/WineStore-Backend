package com.winestore.winestore.service.payment;


import com.winestore.winestore.entity.Order;
import com.winestore.winestore.repository.OrderRepo;
import com.winestore.winestore.util.ESewaSignatureUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${esewa.merchantCode}")
    private String merchantCode;
    @Value("${esewa.secret}")
    private String secret;
    @Value("${esewa.successUrl}")
    private String successUrl;
    @Value("${esewa.failureUrl}")
    private String failureUrl;
    @Value("${esewa.sandboxUrl}")
    private String esewaUrl;

    @Value("${esewa.verifyUrl}")
    private String verifyUrl;

    @Autowired
    private OrderRepo orderRepo;

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public Map<String, String> esewaCreatePaymentRequest(String orderNumber, double amount) throws Exception {
        String transactionId = orderNumber + (int)(Math.random() * 1000);
        Order order=orderRepo.findByOrderNumber(orderNumber).orElseThrow(()->new RuntimeException("Order Dosent exits"));
        order.setTransactionId(transactionId);
        order.setPaymentStatus("pending");

        Map<String, String> params = new HashMap<>();
        params.put("amount", String.valueOf(amount));
        params.put("tax_amount", "0");
        params.put("product_delivery_charge", "0");
        params.put("product_service_charge", "0");
        params.put("total_amount", String.valueOf(amount));
        params.put("transaction_uuid", transactionId);
        params.put("product_code", merchantCode);
        params.put("success_url", successUrl);
        params.put("failure_url", failureUrl);

        // Build data string in same order as docs
        String dataToSign = String.format(
                "total_amount=%s,transaction_uuid=%s,product_code=%s",
                params.get("total_amount"), params.get("transaction_uuid"), params.get("product_code")
        );

        String signature = ESewaSignatureUtil.sign(dataToSign, secret);
        params.put("signature", signature);
        params.put("signed_field_names", "total_amount,transaction_uuid,product_code");

        params.put("esewaUrl", esewaUrl);
        return params;
    }
    public boolean verifyTransaction(String transactionUuid, Double amount, String productCode) {
        String url = verifyUrl +
                "?product_code=" + productCode +
                "&total_amount=" + amount +
                "&transaction_uuid=" + transactionUuid;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getBody() != null && response.getBody().contains("\"status\":\"COMPLETE\"")) {
            // Update DB
            orderRepo.findByTransactionId(transactionUuid).ifPresent(tx -> {
                tx.setPaymentStatus("PAID");
                tx.setPaymentType("esewa");
                orderRepo.save(tx);
            });
            return true;
        } else {
            orderRepo.findByTransactionId(transactionUuid).ifPresent(tx -> {
                tx.setPaymentStatus("UNPAID");
                orderRepo.save(tx);
            });
            return false;
        }
    }

}
