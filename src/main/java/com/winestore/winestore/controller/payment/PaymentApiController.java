package com.winestore.winestore.controller.payment;


import com.winestore.winestore.service.payment.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentApiController {

    private final PaymentService paymentService;

    public PaymentApiController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/init")
    public Map<String, String> initPayment(@RequestParam String orderNumber,
                                           @RequestParam double amount) throws Exception {
        return paymentService.createPaymentRequest(orderNumber, amount);
    }
}
