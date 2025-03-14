package com.example.Boutique_Final.dto;

import lombok.Data;

@Data
public class UpdateQuantityRequest {
    private String productId; // ID of the product
    private String action;    // "increase" or "decrease"

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
