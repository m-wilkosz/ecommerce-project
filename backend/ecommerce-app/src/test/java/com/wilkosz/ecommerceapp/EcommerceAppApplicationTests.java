package com.wilkosz.ecommerceapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.wilkosz.ecommerceapp.dao.OrderRepository;
import com.wilkosz.ecommerceapp.dao.StateRepository;
import com.wilkosz.ecommerceapp.dto.PaymentInfo;
import com.wilkosz.ecommerceapp.dto.Purchase;
import com.wilkosz.ecommerceapp.dto.PurchaseResponse;
import com.wilkosz.ecommerceapp.entity.Order;
import com.wilkosz.ecommerceapp.entity.State;
import com.wilkosz.ecommerceapp.service.CheckoutService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EcommerceAppApplicationTests {

    private CheckoutService checkoutService;
    private StateRepository stateRepository;
    private OrderRepository orderRepository;

    @Autowired
    public EcommerceAppApplicationTests(CheckoutService checkoutService, StateRepository stateRepository,
                                        OrderRepository orderRepository) {
        this.checkoutService = checkoutService;
        this.stateRepository = stateRepository;
        this.orderRepository = orderRepository;
    }

    private String resourceJsonToString(String filename) throws FileNotFoundException {

        InputStream is = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/" + filename + ".json");
        Scanner scan = new Scanner(is).useDelimiter("\\A");
        String jsonString = scan.hasNext() ? scan.next() : "";

        return jsonString;
    }

    // tests of checkout service
    @Test
    public void placeOrderTest() throws FileNotFoundException, JsonProcessingException {

        Purchase purchase = new ObjectMapper().readValue(resourceJsonToString("purchase"), Purchase.class);

        PurchaseResponse purchaseResponse = this.checkoutService.placeOrder(purchase);
        String orderTrackingNumber = purchaseResponse.getOrderTrackingNumber();
        System.out.println(orderTrackingNumber);

        Assertions.assertAll (
                () -> assertNotNull(orderTrackingNumber),
                () -> assertNotEquals("", orderTrackingNumber)
        );
    }

    @Test
    public void createPaymentIntentTest() throws FileNotFoundException, JsonProcessingException, StripeException {

        PaymentInfo paymentInfo = new ObjectMapper().readValue(resourceJsonToString("paymentInfo"), PaymentInfo.class);

        PaymentIntent paymentIntent = this.checkoutService.createPaymentIntent(paymentInfo);

        Assertions.assertAll (
                () -> assertEquals(99, paymentIntent.getAmount()),
                () -> assertEquals("usd", paymentIntent.getCurrency()),
                () -> assertEquals("jkowalski@gmail.com", paymentIntent.getReceiptEmail())
        );
    }

    // test of state repository
    @Test
    public void findByCountryCodeTest() {

        List<State> canadaStates = this.stateRepository.findByCountryCode("CA");

        Assertions.assertAll (
                () -> assertEquals("Alberta", canadaStates.get(0).getName()),
                () -> assertEquals("Nova Scotia", canadaStates.get(6).getName()),
                () -> assertEquals("Yukon", canadaStates.get(12).getName()),
                () -> assertEquals(13, canadaStates.size())
        );
    }

    // test of order repository
    @Test
    public void findByCustomerEmailOrderByDateCreatedDescTest() {

        List<Order> orderList = this.orderRepository
                .findByCustomerEmailOrderByDateCreatedDesc("jkowalski@gmail.com", PageRequest.of(0, 100))
                .getContent();

        List<String> OTNList = new ArrayList<>();
        for (Order order : orderList) {
            OTNList.add(order.getOrderTrackingNumber());
        }

        Assertions.assertAll (
                () -> assertFalse(OTNList.contains(null)),
                () -> assertFalse(OTNList.contains(""))
        );
    }
}