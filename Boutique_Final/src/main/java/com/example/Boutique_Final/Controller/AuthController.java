package com.example.Boutique_Final.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import com.example.Boutique_Final.dto.*;
import com.example.Boutique_Final.model.User;
import com.example.Boutique_Final.service.AuthService;
import com.example.Boutique_Final.service.UserService;
import com.example.Boutique_Final.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import com.example.Boutique_Final.exception.ResourceNotFoundException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "https://example.com"})
public class AuthController {


    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Explicit Constructor Injection
    public AuthController(AuthService authService, UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // 🟢 Register User
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        userService.registerUser(user);
        return ok("Registration successful. Check your email for confirmation.");
    }

    // 🟢 Email Confirmation
    @PostMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@RequestBody EmailConfirmationRequest request) {
        try {
            userService.confirmEmail(request.getEmail(), request.getConfirmationCode());
            return ok().body("Email confirmed successfully.");
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid confirmation code.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate user
            User user = userService.loginUser(request.getEmail(), request.getPassword());

            // Generate JWT token
            String token = jwtService.generateToken(user.getUsername(), user.getEmail());

            // Create a response map
            Map<String, Object> response = new HashMap<>();

            // Add token and user data to the response
            response.put("token", token);

            // IMPORTANT: Format the user id properly. If user.getId() returns an ObjectId,
            // convert it using toHexString() to get a proper 24-character string.
            response.put("userId", user.getId() != null ? user.getId().toHexString() : null);
            response.put("user", user);

            System.out.println("User ID to return: " + user.getId().toHexString());

            return ResponseEntity.ok(response);


        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Login failed"));
        }
    }


//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        try {
//            // Authenticate user
//            User user = userService.loginUser(request.getEmail(), request.getPassword());
//
//            // Generate JWT token
//            String token = jwtService.generateToken(user.getUsername(), user.getEmail());
//
//            // Return both the token and userId
//            Map<String, Object> response = new HashMap<>();
//            response.put("token", token);
//            response.put("userId", user.getId().toString());  // ✅ Fix: Send userId separately
//            response.put("user", user);
//
//            return ResponseEntity.ok(response);
//
//        } catch (IllegalStateException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(Map.of("message", e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("message", "Login failed"));
//        }
//    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ok("Password changed successfully.");
    }

    // Forgot Password
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        userService.forgotPassword(email);
        return ResponseEntity.ok("Password reset email sent.");
    }

    // Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String resetToken = payload.get("resetToken");
        String newPassword = payload.get("newPassword");

        userService.resetPassword(email, resetToken, newPassword);
        return ResponseEntity.ok("Password reset successfully.");
    }



}
