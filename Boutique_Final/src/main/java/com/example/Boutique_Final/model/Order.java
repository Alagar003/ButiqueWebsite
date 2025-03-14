package com.example.Boutique_Final.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.Boutique_Final.model.Order;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private String id;

    @DBRef
    private User user;

    private String address;
    private String phoneNumber;

    private OrderStatus status;
    public enum OrderStatus {
        PREPARING, DELIVERING, DELIVERED, CANCELED
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private LocalDateTime createdAt;

    @DBRef
    private List<OrderItem> items = new ArrayList<>();
}
