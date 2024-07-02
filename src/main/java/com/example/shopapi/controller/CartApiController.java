package com.example.shopapi.controller;

import com.example.shopapi.domain.Cart;
import com.example.shopapi.security.jwt.util.IfLogin;
import com.example.shopapi.security.jwt.util.LoginUserDto;
import com.example.shopapi.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/carts") // http://localhost:8080/carts
@RequiredArgsConstructor
public class CartApiController {
    private final CartService cartService;
    @PostMapping
    public Cart addCart(@IfLogin LoginUserDto loginUserDto) {
        LocalDate localDate = LocalDate.now();
        localDate.getYear();
        localDate.getDayOfMonth();
        localDate.getMonthValue();
        String date = String.valueOf(localDate.getYear()) + (localDate.getMonthValue() < 10 ? "0" :"") + String.valueOf(localDate.getMonthValue()) + (localDate.getDayOfMonth() < 10 ? "0" :"") +String.valueOf(localDate.getDayOfMonth());
        Cart cart = cartService.addCart(loginUserDto.getMemberId(), date);
        return cart;
    }
    @GetMapping // Changed to GET method for fetching cart data
    public ResponseEntity<List<Cart>> getCart(@RequestParam Long memberId) {
        List<Cart> carts = cartService.getCartByMemberId(memberId);
        return ResponseEntity.ok(carts);
    }

}
