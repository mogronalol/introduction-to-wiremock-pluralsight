package com.paybuddy;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

import static com.paybuddy.PayBuddyPaymentResponse.PaymentResponseStatus.SUCCESS;

@RestController
public class PaymentController {

    @PostMapping("/payments")
    public PayBuddyPaymentResponse makePayment(final PayBuddyGatewayRequest payBuddyGatewayRequest) {

        if (new Random().nextInt() % 2 == 0) {
            throw new RuntimeException("Unexpected server error");
        }

        return new PayBuddyPaymentResponse(UUID.randomUUID().toString(), SUCCESS);
    }
}
