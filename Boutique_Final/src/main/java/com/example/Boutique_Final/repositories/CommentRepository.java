package com.example.Boutique_Final.repositories;

import com.example.Boutique_Final.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository <Comment, Long>{
    List<Comment> findByProductId(Long productId);
}
