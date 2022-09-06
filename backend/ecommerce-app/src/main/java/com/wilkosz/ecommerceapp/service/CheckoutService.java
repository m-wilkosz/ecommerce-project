package com.wilkosz.ecommerceapp.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.wilkosz.ecommerceapp.dto.PaymentInfo;
import com.wilkosz.ecommerceapp.dto.Purchase;
import com.wilkosz.ecommerceapp.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}