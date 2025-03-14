//package com.example.Boutique_Final.service;
//
//import com.example.Boutique_Final.Mapper.CartMapper;
//import com.example.Boutique_Final.Mapper.OrderMapper;
//import com.example.Boutique_Final.dto.CartDTO;
//import com.example.Boutique_Final.dto.OrderDTO;
//import com.example.Boutique_Final.exception.InsufficientStockException;
//import com.example.Boutique_Final.exception.ResourceNotFoundException;
//import com.example.Boutique_Final.model.*;
//import com.example.Boutique_Final.repositories.OrderRepository;
//import com.example.Boutique_Final.repositories.ProductRepository;
//import com.example.Boutique_Final.repositories.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.mail.MailException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class OrderService {
//    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
//
//    private final OrderRepository orderRepository;
//    private final CartService cartService;
//    private final ProductRepository productRepository;
//    private final UserRepository userRepository;
//    private final EmailService emailService;
//    private final OrderMapper orderMapper;
//    private final CartMapper cartMapper;
//
////    @Transactional
////    public OrderDTO createOrder(String userId, String address, String phoneNumber) {
////        User user = userRepository.findById(userId)
////                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
////        if (!user.isEmailConfirmation()) {  // Ensure this method is called correctly
////            throw new IllegalStateException("Email not confirmed. Please confirm email before placing an order");
////        }
////        CartDTO cartDTO = cartService.getOrCreateCart(userId);
////        Cart cart = cartMapper.toEntity(cartDTO);
////
////        if (cart.getItems().isEmpty()) {
////            throw new IllegalStateException("Cannot create an order with an empty cart");
////        }
////
////        Order order = new Order();
////        order.setUser(user);
////        order.setAddress(address);
////        order.setPhoneNumber(phoneNumber);
////        order.setStatus(Order.OrderStatus.PREPARING);
////        order.setCreatedAt(LocalDateTime.now());
////
////        List<OrderItem> orderItems = createOrderItems(cart, order);
////        order.setItems(orderItems);
////
////        Order savedOrder = orderRepository.save(order);
////        cartService.clearCart(userId);
////
////        try {
////            emailService.sendOrderConfirmation(savedOrder);
////        } catch (MailException e) {
////            logger.error("Failed to send order confirmation email for order ID " + savedOrder.getId(), e);
////        }
////        return orderMapper.toDTO(savedOrder);
////    }
//
////    private List<OrderItem> createOrderItems(Cart cart, Order order) {
////        return cart.getItems().stream().map(cartItem -> {
////            Product product = productRepository.findById(String.valueOf(cartItem.getProduct().getId()))
////                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + cartItem.getProduct().getId()));
////
////            if (product.getQuantity() == null || product.getQuantity() < cartItem.getQuantity()) {
////                throw new InsufficientStockException("Not enough stock for product " + product.getName());
////            }
////
////            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
////            productRepository.save(product);
////
////            return new OrderItem(null, order, product, cartItem.getQuantity(), product.getPrice());
////        }).collect(Collectors.toList());
////    }
//
//    public List<OrderDTO> getAllOrders() {
//        return orderMapper.toDTOs(orderRepository.findAll());
//    }
//
//    public List<OrderDTO> getUserOrders(String userId) {
//        return orderMapper.toDTOs(orderRepository.findByUserId(Long.parseLong(userId)));
//    }
//
//    public OrderDTO updateOrderStatus(Long orderId, Order.OrderStatus status) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
//        order.setStatus(status);
//        Order updatedOrder = orderRepository.save(order);
//        return orderMapper.toDTO(updatedOrder);
//    }
//}
