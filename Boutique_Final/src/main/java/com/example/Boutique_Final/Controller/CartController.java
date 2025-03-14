package com.example.Boutique_Final.Controller;

import com.example.Boutique_Final.Mapper.CartMapper;
import com.example.Boutique_Final.dto.CartDTO;
import com.example.Boutique_Final.dto.AddToCartRequest;
import com.example.Boutique_Final.dto.UpdateQuantityRequest;
import com.example.Boutique_Final.model.Cart;
import com.example.Boutique_Final.service.CartService;
import com.example.Boutique_Final.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3003")
@RequiredArgsConstructor
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;
    private final JwtService jwtService;
    private final CartMapper cartMapper;

    private String extractToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header format");
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addToCart(@RequestHeader(value = "Authorization", required = false) String token,
                                             @RequestBody AddToCartRequest request) {
        if (token == null) {
            logger.error("Authorization token is missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userId = jwtService.extractUsername(extractToken(token));
            logger.info("Extracted User ID: {}", userId);
            CartDTO cartDTO = cartService.addToCart(userId, request.getProductId(), request.getQuantity());
            return ResponseEntity.ok(cartDTO);
        } catch (Exception ex) {
            logger.error("Error adding to cart: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping()
    public ResponseEntity<CartDTO> getCart(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            logger.error("Authorization token is missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userId = jwtService.extractUsername(extractToken(token));
            logger.info("Fetching cart for user: {}", userId);

            Optional<Cart> cart = cartService.getCartByUserId(userId);
            return cart.map(value -> ResponseEntity.ok(cartMapper.toDTO(value)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception ex) {
            logger.error("Error fetching cart: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

        @GetMapping("/{userId}")
        public ResponseEntity<Optional<Cart>> getCartByid(@PathVariable String userId) {
            Optional<Cart> cart = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(cart);
        }



    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            logger.error("Authorization token is missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userId = jwtService.extractUsername(extractToken(token));
            cartService.clearCart(userId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            logger.error("Error clearing cart: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/updateQuantity")
    public ResponseEntity<CartDTO> updateCartItemQuantity(@RequestHeader(value = "Authorization", required = false) String token,
                                                          @RequestBody UpdateQuantityRequest request) {
        if (token == null) {
            logger.error("Authorization token is missing.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userId = jwtService.extractUsername(extractToken(token));
            CartDTO updatedCart = cartService.updateItemQuantity(userId, request.getProductId(), request.getAction());
            return ResponseEntity.ok(updatedCart);
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid request: {}", ex.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            logger.error("Error updating cart item quantity: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }


}
