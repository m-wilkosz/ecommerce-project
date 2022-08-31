package com.wilkosz.ecommerceapp.dao;

import com.wilkosz.ecommerceapp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String theEmail);
}