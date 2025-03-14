package com.example.Boutique_Final.repositories;

import com.example.Boutique_Final.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    // 🟢 Query to fetch products without comments (pagination supported)
    @Query("{ 'comments': { $exists: false } }")
    Page<Product> findAllWithoutComments(Pageable pageable);

    // 🟢 Search by name or description (case-insensitive search using regex)
    @Query("{ '$or': [ { 'name': { $regex: ?0, $options: 'i' } }, { 'description': { $regex: ?1, $options: 'i' } } ] }")
    List<Product> searchByNameOrDescription(String nameKeyword, String descriptionKeyword);

    // 🟢 Find products by category, ignoring case
    List<Product> findByCategoryIgnoreCase(String category);

    // 🟢 Default method to get all products with pagination
    Page<Product> findAll(Pageable pageable);

    // 🟢 Fetch products by category with pagination and sorting
    @Query("{ 'category': { $regex: ?0, $options: 'i' } }")
    Page<Product> findByCategory(String category, Pageable pageable);

    // 🟢 Advanced filter: category and price range (custom query)
    @Query("{ '$and': [ { 'category': { $regex: ?0, $options: 'i' } }, { 'price': { $gte: ?1, $lte: ?2 } } ] }")
    List<Product> findByCategoryAndPriceRange(String category, double minPrice, double maxPrice);

    // 🟢 Filter active products (for soft delete use cases)
    @Query("{ 'isDeleted': false }")
    Page<Product> findAllActive(Pageable pageable);

    // 🟢 Count the number of products in a specific category
    @Query("{ 'category': { $regex: ?0, $options: 'i' } }")
    long countByCategory(String category);
}
