package com.example.Boutique_Final.Mapper;

import com.example.Boutique_Final.dto.CartItemDTO;
import com.example.Boutique_Final.model.CartItem;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapperHelper {

    @Named("toCartItemDTOs")
    public static List<CartItemDTO> toCartItemDTOs(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return cartItems.stream()
                .map(CartMapperHelper::toCartItemDTO)
                .collect(Collectors.toList());
    }

    @Named("toCartItemDTO")
    public static CartItemDTO toCartItemDTO(CartItem cartItem) {
        if (cartItem == null || cartItem.getProduct() == null) {
            return null;
        }
        return new CartItemDTO(
                cartItem.getProduct().getId().toHexString(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getPrice(),
                cartItem.getQuantity(),
                cartItem.getProduct().getImage()
        );
    }
}
