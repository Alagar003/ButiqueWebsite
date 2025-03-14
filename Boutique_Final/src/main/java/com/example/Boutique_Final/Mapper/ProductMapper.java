package com.example.Boutique_Final.Mapper;

import com.example.Boutique_Final.dto.ProductDTO;
import com.example.Boutique_Final.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", expression = "java(product.getId() != null ? product.getId().toHexString() : null)") // Convert ObjectId to String
    @Mapping(target = "image", source = "image")
    ProductDTO toDTO(Product product);

    @Mapping(target = "id", expression = "java(productDTO.getId() != null ? new org.bson.types.ObjectId(productDTO.getId()) : null)") // Convert String to ObjectId
    @Mapping(target = "image", source = "image")
    Product toEntity(ProductDTO productDTO);

//    @Mapping(target = "userId", expression = "java(comment.getUser() != null && comment.getUser().getId() != null ? comment.getUser().getId().toHexString() : null)") // Convert ObjectId to String
//    CommentDTO toDTO(Comment comment);
//
//    @Mapping(target = "user.id", expression = "java(commentDTO.getUserId() != null ? new org.bson.types.ObjectId(commentDTO.getUserId()) : null)") // Convert String to ObjectId
//    @Mapping(target = "product", ignore = true)
//    Comment toEntity(CommentDTO commentDTO);

    // Helper methods for lists can be added if needed.
}
