package com.winestore.winestore.controller.payment;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.winestore.winestore.ApiResponse.ApiResponse;
import com.winestore.winestore.service.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentApiController {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PaymentApiController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/init")
    public Map<String, String> initPayment(@RequestParam String orderNumber,
                                           @RequestParam double amount) throws Exception {
        return paymentService.esewaCreatePaymentRequest(orderNumber, amount);
    }
    @GetMapping("/success")
    public ResponseEntity<ApiResponse<?>> handleSuccess(@RequestParam("data") String encodedData) throws Exception {
        // Decode Base64 → JSON
        String json = new String(Base64.getDecoder().decode(encodedData));
        Map<String, Object> payload = objectMapper.readValue(json, Map.class);

        String transactionUuid = (String) payload.get("transaction_uuid");
        String amount = (String) payload.get("total_amount");
        String productCode = (String) payload.get("product_code");

        // Verify with eSewa
        boolean verified = paymentService.verifyTransaction(transactionUuid, Double.valueOf(amount), productCode);
        String msg=verified?"Payment Completed":"Payment is not done";
        return ResponseEntity.ok(
                new ApiResponse<>(true, msg,verified ? "Payment Verified ✅" : "Payment Failed ❌")
        );
    }
}
