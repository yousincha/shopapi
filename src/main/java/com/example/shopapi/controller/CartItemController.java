package com.example.shopapi.controller;

import com.example.shopapi.domain.CartItem;
import com.example.shopapi.dto.AddCartItemDto;
import com.example.shopapi.dto.UpdateQuantityRequest;
import com.example.shopapi.security.jwt.util.IfLogin;
import com.example.shopapi.security.jwt.util.LoginUserDto;
import com.example.shopapi.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PutMapping("/{itemId}")
    public ResponseEntity<String> updateCartItemQuantity(
            @PathVariable Long itemId,
            @RequestBody UpdateQuantityRequest request) {

        try {
            // Update cart item quantity
            cartItemService.updateCartItemQuantity(itemId, request.getQuantity());
            return ResponseEntity.ok("Cart item quantity updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating cart item quantity: " + e.getMessage());
        }
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
    // CORS 설정 추가
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/cancelCartItems")
    public ResponseEntity cancelCartItemsWithCors(@IfLogin LoginUserDto loginUserDto, @RequestParam List<Long> itemIds) { // 수정된 부분: @RequestParam 사용
        try {
            for (Long itemId : itemIds) {
                if(cartItemService.isCartItemExist(loginUserDto.getMemberId(), itemId)) {
                    cartItemService.cancelCartItem(loginUserDto.getMemberId(), itemId);
                }
            }
            return ResponseEntity.ok("상품이 장바구니에서 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("상품 삭제에 실패했습니다.");
        }
    }
}
