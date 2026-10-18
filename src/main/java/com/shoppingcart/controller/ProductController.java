package com.shoppingcart.controller;

import com.shoppingcart.dto.ProductRequest;
import com.shoppingcart.dto.ProductResponse;
import com.shoppingcart.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;


    public ProductController(
            ProductService productService) {

        this.productService = productService;
    }


    // ==========================================
    // ADD PRODUCT
    // ADMIN ONLY
    // ==========================================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> addProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response =
                productService.addProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ==========================================
    // GET ALL PRODUCTS
   
    // ==========================================

    @GetMapping
    public ResponseEntity<List<ProductResponse>>
    getAllProducts() {

        List<ProductResponse> products =
                productService.getAllProducts();

        return ResponseEntity.ok(products);
    }


    // ==========================================
    // GET PRODUCT BY ID
    // ==========================================

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse>
    getProductById(
            @PathVariable Long productId) {

        ProductResponse response =
                productService.getProductById(
                        productId
                );

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // UPDATE PRODUCT
    // Admin ONLY
    // ==========================================

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse>
    updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response =
                productService.updateProduct(
                        productId,
                        request
                );

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // DELETE PRODUCT
    //ADMIN ONLY
    // ==========================================

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long productId) {

        productService.deleteProduct(productId);

        return ResponseEntity.ok(
                "Product deleted successfully"
        );
    }


    // ==========================================
    // SEARCH PRODUCT
    // ==========================================

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>>
    searchProducts(
            @RequestParam String name) {

        List<ProductResponse> products =
                productService.searchProducts(name);

        return ResponseEntity.ok(products);
    }


    // ==========================================
    // PRODUCTS BY CATEGORY
    // ==========================================

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>>
    getProductsByCategory(
            @PathVariable String category) {

        List<ProductResponse> products =
                productService.getProductsByCategory(
                        category
                );

        return ResponseEntity.ok(products);
    }
}