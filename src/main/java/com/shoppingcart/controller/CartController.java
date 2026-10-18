package com.shoppingcart.controller;

import com.shoppingcart.model.Cart;
import com.shoppingcart.model.CartItem;
import com.shoppingcart.service.CartService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // ==========================================
    // ADD PRODUCT TO CART
    // ==========================================

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        Cart cart = cartService.addToCart(
                userId,
                productId,
                quantity
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cart);
    }

 // ==========================================
 // UPDATE CART ITEM QUANTITY
 // ==========================================

 @PutMapping("/update")
 public ResponseEntity<CartItem> updateCartQuantity(
         @RequestParam Long userId,
         @RequestParam Long cartItemId,
         @RequestParam Integer quantity) {

     CartItem updatedCartItem =
             cartService.updateCartQuantity(
                     userId,
                     cartItemId,
                     quantity
             );

     return ResponseEntity.ok(updatedCartItem);
 }
    
//starting from this line is temporary for testing 
    @GetMapping("/test")
    public String testCart() {
        return "Cart Controller is working";
    }
    //end
    
    // ==========================================
    // VIEW CART
    // ==========================================
    
    //staring from this line is commented 
//    @GetMapping
//    public ResponseEntity<Cart> getCart(
//            @RequestParam Long userId) {
//
//        Cart cart = cartService.getCart(userId);
//
//        return ResponseEntity.ok(cart);
//    }
//end 
    
    //and for temporary use i use this code from this line 
    //start
    @GetMapping
    public ResponseEntity<Cart> getCart(
            @RequestParam Long userId) {

        System.out.println("GET CART CALLED, userId = " + userId);

        Cart cart = cartService.getCart(userId);

        System.out.println("CART RESULT = " + cart);

        return ResponseEntity.ok(cart);
    }
    //end

    // ==========================================
    // VIEW CART ITEMS
    // ==========================================

    @GetMapping("/items")
    public ResponseEntity<List<CartItem>> getCartItems(
            @RequestParam Long userId) {

        List<CartItem> cartItems =
                cartService.getCartItems(userId);

        return ResponseEntity.ok(cartItems);
    }


    // ==========================================
    // CALCULATE CART TOTAL
    // ==========================================

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> calculateCartTotal(
            @RequestParam Long userId) {

        BigDecimal total =
                cartService.calculateCartTotal(userId);

        return ResponseEntity.ok(total);
    }


    // ==========================================
    // REMOVE ITEM FROM CART
    // ==========================================

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<String> removeFromCart(
            @PathVariable Long cartItemId,
            @RequestParam Long userId) {

        cartService.removeFromCart(
                userId,
                cartItemId
        );

        return ResponseEntity.ok(
                "Cart item removed successfully"
        );
    }


    // ==========================================
    // CLEAR CART
    // ==========================================

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(
            @RequestParam Long userId) {

        cartService.clearCart(userId);

        return ResponseEntity.ok(
                "Cart cleared successfully"
        );
    }
}