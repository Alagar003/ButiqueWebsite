package com.example.Boutique_Final.Mapper;

import com.example.Boutique_Final.dto.CartDTO;
import com.example.Boutique_Final.model.Cart;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = CartMapperHelper.class)
public interface CartMapper {

    @Mapping(target = "id", source = "id", qualifiedByName = "objectIdToString")
    @Mapping(target = "userId", source = "userId", qualifiedByName = "objectIdToString")
    CartDTO toDTO(Cart cart);

    @Mapping(target = "id", source = "id", qualifiedByName = "stringToObjectId")
    @Mapping(target = "userId", source = "userId", qualifiedByName = "stringToObjectId")
    Cart toEntity(CartDTO cartDTO);

    // Convert String to ObjectId
    @Named("stringToObjectId")
    public static ObjectId stringToObjectId(String id) {
        return (id != null && !id.isEmpty()) ? new ObjectId(id) : null;
    }

    // Convert ObjectId to String
    @Named("objectIdToString")
    public static String objectIdToString(ObjectId id) {
        return (id != null) ? id.toHexString() : null;
    }
}
