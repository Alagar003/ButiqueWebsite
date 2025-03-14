package com.example.Boutique_Final.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemDTO {
    private String productId; // Represents product.id
    private String productName; // Represents product.name
    private Double price; // Represents product.price
    private Integer quantity; // Represents CartItem.quantity
    private String imageUrl;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CartItemDTO(String productId, String productName, Double price, Integer quantity, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Utility method to safely convert BigDecimal to Double
    public static Double convertBigDecimalToDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : null; // Handle null values safely
    }
}
