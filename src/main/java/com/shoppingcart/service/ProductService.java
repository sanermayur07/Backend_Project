package com.shoppingcart.service;

import com.shoppingcart.dto.ProductRequest;

import com.shoppingcart.repository.OrderItemRepository;
import com.shoppingcart.dto.ProductResponse;
import com.shoppingcart.exception.ResourceNotFoundException;
import com.shoppingcart.model.Product;
import com.shoppingcart.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;


    public ProductService(ProductRepository productRepository,
    		OrderItemRepository orderItemRepository) {
    	
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }


    // ==========================================
    // ADD PRODUCT
    // ==========================================

    public ProductResponse addProduct(
            ProductRequest request) {

        Product product = new Product();

        product.setName(
                request.getName().trim()
        );

        product.setDescription(
                request.getDescription() != null
                        ? request.getDescription().trim()
                        : null
        );

        product.setPrice(
                request.getPrice()
        );

        product.setCategory(
                request.getCategory() != null
                        ? request.getCategory().trim()
                        : null
        );

        product.setQuantity(
                request.getQuantity()
        );

        product.setImage(
                request.getImage()
        );


        Product savedProduct =
                productRepository.save(product);


        return convertToResponse(savedProduct);
    }


    // ==========================================
    // GET ALL PRODUCTS
    // ==========================================

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    // ==========================================
    // GET PRODUCT BY ID
    // ==========================================

    @Transactional(readOnly = true)
    public ProductResponse getProductById(
            Long productId) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with ID: "
                                		+ productId
                                )
                        );

        return convertToResponse(product);
    }


    // ==========================================
    // UPDATE PRODUCT
    // ==========================================

    public ProductResponse updateProduct(
            Long productId,
            ProductRequest request) {

        Product existingProduct =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with ID: "
                                				+ productId
                                )
                        );


        existingProduct.setName(
                request.getName().trim()
        );

        existingProduct.setDescription(
                request.getDescription() != null
                        ? request.getDescription().trim()
                        : null
        );

        existingProduct.setPrice(
                request.getPrice()
        );

        existingProduct.setCategory(
                request.getCategory() != null
                        ? request.getCategory().trim()
                        : null
        );

        existingProduct.setQuantity(
                request.getQuantity()
        );

        existingProduct.setImage(
                request.getImage()
        );


        Product updatedProduct =
                productRepository.save(
                        existingProduct
                );


        return convertToResponse(updatedProduct);
    }


    // ==========================================
    // DELETE PRODUCT
    // ==========================================

    public void deleteProduct(Long productId) {

        if (!productRepository.existsById(productId)) {

            throw new ResourceNotFoundException(
                    "Product not found with ID: "
            				+ productId
            );
        }

        // Check whether product is already used in an order
        if (orderItemRepository.existsByProduct_ProductId(productId)) {

            throw new IllegalArgumentException(
                    "Cannot delete product because it is already used in an order."
            );
        }
        
        //Delete product
        productRepository.deleteById(productId);
    }


    // ==========================================
    // SEARCH PRODUCT
    // ==========================================

    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(
            String name) {

        return productRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    // ==========================================
    // GET PRODUCTS BY CATEGORY
    // ==========================================

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(
            String category) {

        return productRepository
                .findByCategoryIgnoreCase(category)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    // ==========================================
    // ENTITY → DTO
    // ==========================================

    private ProductResponse convertToResponse(
            Product product) {

        return new ProductResponse(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getQuantity(),
                product.getImage()
        );
    }
}