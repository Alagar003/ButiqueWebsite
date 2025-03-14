package com.example.Boutique_Final.service;

import com.example.Boutique_Final.Mapper.CartMapper;
import com.example.Boutique_Final.dto.CartDTO;
import com.example.Boutique_Final.dto.CartItemDTO;
import com.example.Boutique_Final.exception.ResourceNotFoundException;
import com.example.Boutique_Final.exception.UserNotFoundException;
import com.example.Boutique_Final.model.Cart;
import com.example.Boutique_Final.model.CartItem;
import com.example.Boutique_Final.model.Product;
import com.example.Boutique_Final.model.User;
import com.example.Boutique_Final.repositories.CartRepository;
import com.example.Boutique_Final.repositories.ProductRepository;
import com.example.Boutique_Final.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository, UserRepository userRepository,
                       ProductRepository productRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
    }

    public CartDTO getOrCreateCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));
        return getOrCreateCart(user);
    }

    public CartDTO getOrCreateCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> createNewCart(user));

        List<CartItemDTO> cartItemDTOs = cart.getItems().stream()
                .map(this::mapToCartItemDTO)
                .collect(Collectors.toList());

        return new CartDTO(cart.getId().toHexString(), user.getId().toHexString(), user.getEmail(),
                cartItemDTOs, calculateTotalPrice(cartItemDTOs));
    }

    private Cart createNewCart(User user) {
        Cart newCart = new Cart(new ObjectId(), user.getId(), user, new ArrayList<>());
        logger.info("Creating new cart for user: {}", user.getEmail());
        return cartRepository.save(newCart);
    }

    public CartDTO getCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + user.getId()));

        List<CartItemDTO> cartItemDTOs = cart.getItems().stream()
                .map(this::mapToCartItemDTO)
                .collect(Collectors.toList());

        return new CartDTO(cart.getId().toHexString(), user.getId().toHexString(), user.getEmail(),
                cartItemDTOs, calculateTotalPrice(cartItemDTOs));
    }

    public CartDTO addToCart(String email, String productId, int quantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> createNewCart(user));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        updateCartItem(cart, product, quantity, "increase");
        return cartMapper.toDTO(cartRepository.save(cart));
    }

    public CartDTO updateItemQuantity(String email, String productId, String action) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + user.getId()));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        updateCartItem(cart, product, 1, action);
        return cartMapper.toDTO(cartRepository.save(cart));
    }

    public void clearCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + user.getId()));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public void removeCartItem(String email, String productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + user.getId()));

        Iterator<CartItem> iterator = cart.getItems().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getProduct().getId().equals(productId)) {
                iterator.remove();
            }
        }
        cartRepository.save(cart);
    }

    private void updateCartItem(Cart cart, Product product, int quantity, String action) {
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        existingItem.ifPresentOrElse(
                item -> {
                    if ("increase".equals(action)) {
                        item.setQuantity(item.getQuantity() + quantity);
                    } else if ("decrease".equals(action) && item.getQuantity() > quantity) {
                        item.setQuantity(item.getQuantity() - quantity);
                    } else {
                        cart.getItems().remove(item);
                    }
                },
                () -> {
                    if ("increase".equals(action)) {
                        cart.getItems().add(new CartItem(product, quantity));
                    }
                }
        );
    }

    private CartItemDTO mapToCartItemDTO(CartItem item) {
        Product product = item.getProduct();
        return new CartItemDTO(product.getId().toHexString(), product.getName(),
                product.getPrice(), item.getQuantity(), product.getDescription());
    }

    private Double calculateTotalPrice(List<CartItemDTO> cartItemDTOs) {
        return cartItemDTOs.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }


    public Optional<Cart> getCartByUserId(String userId) {
        try {
            // Create an ObjectId instance from the provided userId string
            ObjectId oid = new ObjectId(userId);
            return cartRepository.findByUserId(oid);
        } catch (IllegalArgumentException e) {
            // If userId is not a valid ObjectId string, return an empty Optional
            return Optional.empty();
        }
    }
    public Cart getCartByEmail(String email) {
        return cartRepository.findByUserEmail(email).orElse(null);
    }


}
