package com.example.Boutique_Final.repositories;

import com.example.Boutique_Final.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // 🟢 Find user by email
    Optional<User> findByEmail(String email);

    // 🟢 Find user by email and confirmation code
    Optional<User> findByEmailAndConfirmationCode(String email, String confirmationCode);

    // 🟢 Check if email already exists
    boolean existsByEmail(String email);

}
