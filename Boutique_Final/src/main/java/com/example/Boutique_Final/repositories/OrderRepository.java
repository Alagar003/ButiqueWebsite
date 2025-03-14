package com.example.Boutique_Final.repositories;


import com.example.Boutique_Final.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, Long> {
    List<Order> findByUserId(long userId);
}
