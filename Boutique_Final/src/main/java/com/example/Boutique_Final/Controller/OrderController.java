//package com.example.Boutique_Final.Controller;
//
//import com.example.Boutique_Final.dto.OrderDTO;
//import com.example.Boutique_Final.model.Order;
//import com.example.Boutique_Final.service.OrderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/orders")
//@RequiredArgsConstructor
//@CrossOrigin
//public class OrderController {
//
//    private final OrderService orderService;
//
//    @PostMapping("/create")
//    public ResponseEntity<OrderDTO> createOrder(@RequestBody Map<String, Object> payload) {
//        String userId = (String) payload.get("userId");
//        String address = (String) payload.get("address");
//        String phoneNumber = (String) payload.get("phoneNumber");
//
//        OrderDTO orderDTO = orderService.createOrder(userId, address, phoneNumber);
//        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<OrderDTO>> getAllOrders() {
//        List<OrderDTO> orders = orderService.getAllOrders();
//        return new ResponseEntity<>(orders, HttpStatus.OK);
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable String userId) {
//        List<OrderDTO> orders = orderService.getUserOrders(userId);
//        return new ResponseEntity<>(orders, HttpStatus.OK);
//    }
//
//    @PutMapping("/{orderId}/status")
//    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, Object> payload) {
//        Order.OrderStatus status = Order.OrderStatus.valueOf((String) payload.get("status"));
//        OrderDTO orderDTO = orderService.updateOrderStatus(orderId, status);
//        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
//    }
//}
