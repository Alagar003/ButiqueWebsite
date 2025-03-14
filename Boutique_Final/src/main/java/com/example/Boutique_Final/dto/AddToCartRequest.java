package com.example.Boutique_Final.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


@Data
public class AddToCartRequest {
    @NotBlank
    private String productId;

    public @NotBlank String getProductId() {
        return productId;
    }

    public void setProductId(@NotBlank String productId) {
        this.productId = productId;
    }

    public @NotNull @Positive Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull @Positive Integer quantity) {
        this.quantity = quantity;
    }

    @NotNull
    @Positive
    private Integer quantity;
}
