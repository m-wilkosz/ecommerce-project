package com.wilkosz.ecommerceapp.dto;

import com.wilkosz.ecommerceapp.entity.Address;
import com.wilkosz.ecommerceapp.entity.Customer;
import com.wilkosz.ecommerceapp.entity.Order;
import com.wilkosz.ecommerceapp.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}