package com.wilkosz.ecommerceapp.dao;

import com.wilkosz.ecommerceapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}