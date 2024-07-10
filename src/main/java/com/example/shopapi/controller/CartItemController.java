package com.example.shopapi.controller;

import com.example.shopapi.domain.CartItem;
import com.example.shopapi.dto.AddCartItemDto;
import com.example.shopapi.security.jwt.util.IfLogin;
import com.example.shopapi.security.jwt.util.LoginUserDto;
import com.example.shopapi.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/cartItems")
@RestController
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping
    public CartItem addCartItem(@IfLogin LoginUserDto loginUserDto, @RequestBody AddCartItemDto addCartItemDto){
        // 같은 cart에 같은 product가 있으면 quantity를 더해줘야함
        if(cartItemService.isCartItemExist(loginUserDto.getMemberId(), addCartItemDto.getCartId(), addCartItemDto.getProductId())){
            CartItem cartItem = cartItemService.getCartItem(loginUserDto.getMemberId(), addCartItemDto.getCartId(), addCartItemDto.getProductId());
            cartItem.setQuantity(cartItem.getQuantity() + addCartItemDto.getQuantity());
            return cartItemService.updateCartItem(cartItem);
        }
        return cartItemService.addCartItem(addCartItemDto);
    }

    @GetMapping
    public List<CartItem> getCartItems(@IfLogin LoginUserDto loginUserDto, @RequestParam(required = false) Long cartId) {
        if(cartId == null)
            return cartItemService.getCartItems(loginUserDto.getMemberId());
        return cartItemService.getCartItems(loginUserDto.getMemberId(), cartId);
    }

    @DeleteMapping("/deleteAfterPayment")
    public ResponseEntity deleteCartItemsAfterPayment(@IfLogin LoginUserDto loginUserDto, @RequestBody List<Long> cartItemIds) {
        for (Long cartItemId : cartItemIds) {
            if(cartItemService.isCartItemExist(loginUserDto.getMemberId(), cartItemId)) {
                cartItemService.deleteCartItem(loginUserDto.getMemberId(), cartItemId);
            }
        }
        return ResponseEntity.ok().build();
    }
}
