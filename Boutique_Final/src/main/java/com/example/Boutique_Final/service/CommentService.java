//package com.example.Boutique_Final.service;
//import com.example.Boutique_Final.Mapper.CommentMapper;
//import com.example.Boutique_Final.dto.CommentDTO;
//import com.example.Boutique_Final.exception.ResourceNotFoundException;
//import com.example.Boutique_Final.model.Comment;
//import com.example.Boutique_Final.model.Product;
//import com.example.Boutique_Final.model.User;
//import com.example.Boutique_Final.repositories.CommentRepository;
//import com.example.Boutique_Final.repositories.ProductRepository;
//import com.example.Boutique_Final.repositories.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class CommentService {
//    private final CommentRepository commentRepository;
//    private final ProductRepository productRepository;
//    private final UserRepository userRepository;
//    private final CommentMapper commentMapper;
////
////    public CommentDTO addComment(Long productId, String userId, CommentDTO commentDTO){
////        Product product = productRepository.findById(Long.valueOf(String.valueOf(productId)))
////                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));
////        User user = userRepository.findById(userId)
////                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
////
////        Comment comment = commentMapper.toEntity(commentDTO);
////        comment.setProduct(product);
////        comment.setUser(user);
////        Comment savedComment = commentRepository.save(comment);
////        return commentMapper.toDTO(savedComment);
////
////    }
//
//    public List<CommentDTO> getCommentsByProduct(Long productId){
//        List<Comment> comments = commentRepository.findByProductId(productId);
//        return comments.stream()
//                .map(commentMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//}