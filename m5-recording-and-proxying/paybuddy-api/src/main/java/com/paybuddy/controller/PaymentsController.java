package com.paybuddy.controller;

import com.paybuddy.dto.PayBuddyPaymentRequest;
import com.paybuddy.dto.PayBuddyPaymentResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.paybuddy.dto.PayBuddyPaymentResponse.PaymentResponseStatus.SUCCESS;

@RestController
public class PaymentsController {

    @PostMapping("/payments")
    public PayBuddyPaymentResponse getPaymentById(final PayBuddyPaymentRequest payBuddyPaymentRequest) {
        return new PayBuddyPaymentResponse(RandomStringUtils.randomNumeric(10), SUCCESS);
    }
}
