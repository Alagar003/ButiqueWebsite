package com.example.Boutique_Final.model;

import com.example.Boutique_Final.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private String id;

    @DBRef
    private Product product; // Reference to the Product collection

    private Integer quantity;
    private Double price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CartItem(Product product, Integer quantity) {
        this.product = product;
        this.setQuantity(quantity); // Use setter for validation
    }

    public void setQuantity(Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Quantity must be a positive value.");
        }
        this.quantity = quantity;
    }
}
