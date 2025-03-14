package com.example.Boutique_Final.dto;

import com.example.Boutique_Final.model.Cart;
import com.example.Boutique_Final.model.CartItem;
import com.example.Boutique_Final.model.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor // Default no-arg constructor for MapStruct
public class CartDTO {
    private static final Logger logger = LoggerFactory.getLogger(CartDTO.class);

    private String id; // Converted from ObjectId
    private String userId; // Converted from ObjectId
    private String userEmail;
    private List<CartItemDTO> cartItems;
    private Double totalPrice;

    // Constructor to initialize all fields
    public CartDTO(String id, String userId, String userEmail, List<CartItemDTO> cartItems, Double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
    }

    // Constructor to map Cart to CartDTO
    public CartDTO(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }

        try {
            // Convert ObjectId to String for cart id
            this.id = cart.getId() != null ? cart.getId().toHexString() : null;

            if (cart.getUser() == null) {
                throw new IllegalArgumentException("User in Cart cannot be null");
            }

            // Convert ObjectId to String for user id
            this.id = cart.getId() != null ? cart.getId().toHexString() : null;

            this.userEmail = cart.getUser().getEmail();

            // Map CartItems to CartItemDTOs
            this.cartItems = cart.getItems() != null
                    ? cart.getItems().stream().map(this::mapToCartItemDTO).collect(Collectors.toList())
                    : List.of();

            // Calculate the total price
            this.totalPrice = calculateTotalPrice(this.cartItems);

        } catch (Exception e) {
            logger.error("Error creating CartDTO: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating CartDTO: " + e.getMessage());
        }
    }

    // Utility method to map CartItem to CartItemDTO
    private CartItemDTO mapToCartItemDTO(CartItem cartItem) {
        if (cartItem == null) {
            throw new IllegalArgumentException("CartItem cannot be null");
        }

        Product product = cartItem.getProduct();
        if (product == null) {
            throw new IllegalArgumentException("Product in CartItem cannot be null");
        }

        return new CartItemDTO(
                product.getId() instanceof ObjectId ? ((ObjectId) product.getId()).toHexString() : null, // Cast before calling
                product.getName(),
                product.getPrice(),
                cartItem.getQuantity(),
                product.getImage()
        );

    }

    // Utility method to calculate total price
    private Double calculateTotalPrice(List<CartItemDTO> cartItems) {
        return cartItems != null && !cartItems.isEmpty()
                ? cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum()
                : 0.0;
    }
}
