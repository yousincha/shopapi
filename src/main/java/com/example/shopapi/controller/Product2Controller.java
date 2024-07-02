package com.example.shopapi.controller;

import com.example.shopapi.domain.Product;
import com.example.shopapi.domain.Product2;
import com.example.shopapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/products2")
@RequiredArgsConstructor
public class Product2Controller {
    private final ProductService productService;
    @GetMapping
    public Page<Product> getProduct2(@RequestParam(required = false, defaultValue = "0")Long categoryId, @RequestParam(required = false, defaultValue = "0")int page) {

        if (categoryId == 0)
            return productService.getProducts (page, 9);
        else {
            Page<Product> products = productService.getProducts (categoryId, page, 9);
            return products;
        }
    }
}
