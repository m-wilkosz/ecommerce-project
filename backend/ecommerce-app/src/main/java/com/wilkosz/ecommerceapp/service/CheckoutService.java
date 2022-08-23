package com.wilkosz.ecommerceapp.service;

import com.wilkosz.ecommerceapp.dto.Purchase;
import com.wilkosz.ecommerceapp.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);
}