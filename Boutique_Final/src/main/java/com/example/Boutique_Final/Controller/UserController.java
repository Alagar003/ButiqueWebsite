package com.example.Boutique_Final.Controller;

import com.example.Boutique_Final.dto.EmailRequest;
import com.example.Boutique_Final.model.User;
import com.example.Boutique_Final.service.JwtService;
import com.example.Boutique_Final.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    // Endpoint to get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //update
    @PostMapping("/role")
    public ResponseEntity<String> getUserRole(@Valid @RequestBody EmailRequest request) {
        User user = userService.getUserByEmail(request.getEmail());

        if (user != null) {
            String role = String.valueOf(user.getRole());
            return ResponseEntity.ok(role);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint to get the currently logged-in user's details
//    @GetMapping("/user")
//    public ResponseEntity<?> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authHeader) {
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
//        }
//
//        try {
//            String token = authHeader.substring(7); // Remove "Bearer " prefix
//            String email = jwtService.extractUsername(token);
//
//            User user = userService.getUserByEmail(email);
//            if (user != null) {
//                return ResponseEntity.ok(user);
//            } else {
//                return ResponseEntity.status(404).body("User not found");
//            }
//        } catch (ExpiredJwtException e) {
//            return ResponseEntity.status(401).body("Token expired");
//        } catch (MalformedJwtException e) {
//            return ResponseEntity.status(401).body("Invalid token format");
//        } catch (SignatureException e) {
//            return ResponseEntity.status(401).body("Invalid token signature");
//        } catch (Exception e) {
//            return ResponseEntity.status(401).body("Invalid or expired token");
//        }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        try {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            String email = jwtService.extractUsername(token);

            User user = userService.getUserByEmail(email);
            if (user != null) {
                // Convert ObjectId to string
                Map<String, Object> userResponse = new HashMap<>();
                userResponse.put("id", user.getId().toString()); // Convert ObjectId to String
                userResponse.put("name", user.getUsername());
                userResponse.put("email", user.getEmail());
                userResponse.put("role", user.getRole());

                return ResponseEntity.ok(userResponse);
            } else {
                return ResponseEntity.status(404).body("User not found");
            }
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body("Token expired");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(401).body("Invalid token format");
        } catch (SignatureException e) {
            return ResponseEntity.status(401).body("Invalid token signature");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }



}




