package com.example.Boutique_Final.service;

import com.example.Boutique_Final.dto.CommentDTO;
import com.example.Boutique_Final.dto.ProductDTO;
import com.example.Boutique_Final.dto.ProductListDTO;
import com.example.Boutique_Final.model.Product;
import com.example.Boutique_Final.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Get all products without comments
    public Page<ProductListDTO> findAllWithoutComments(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(product -> new ProductListDTO(
                product.getId().toHexString(),
                product.getName(),
                product.getDescription(),
                product.getPrice().doubleValue(),
                product.getQuantity(),
                product.getImage()
        ));
    }

    // Get product by ID
    public ProductDTO getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);

        return product.map(value -> new ProductDTO(
                value.getId().toHexString(),               // id
                value.getName(),             // name
                value.getDescription(),      // description
                value.getPrice(),            // price
                value.getQuantity(),         // quantity
                value.getImage(),            // image
                value.getCategory(),         // category
                new ArrayList<>()            // comments (empty for now)
        )).orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
    }


    // Get products by category
    public List<ProductListDTO> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategoryIgnoreCase(category);
        return products.stream()
                .map(product -> new ProductListDTO(
                        product.getId().toHexString(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice().doubleValue(),
                        product.getQuantity(),
                        product.getImage()
                ))
                .collect(Collectors.toList());
    }

    // Create a new product
    public ResponseEntity<ProductDTO> createProduct(ProductDTO productDTO) {
        // Validate fields if necessary
        validateProduct(productDTO);

        // Construct a Product entity from the incoming DTO
        Product product = new Product(
                null, // ID is null for new product; MongoDB will generate it.
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getQuantity(),
                productDTO.getImage(),
                productDTO.getCategory(), // Add category here
                null // comments can be null or empty initially.
        );

        // Save the product in the repository
        Product savedProduct = productRepository.save(product);

        // Construct a ProductDTO to return as the response
        ProductDTO savedProductDTO = new ProductDTO(
                savedProduct.getId().toHexString(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getPrice(),
                savedProduct.getQuantity(),
                savedProduct.getImage(),
                savedProduct.getCategory(), // Return the saved category
                null
        );

        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    // Delete a product
    public ResponseEntity<Void> deleteProduct(String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Search products by name or description
    public List<ProductListDTO> searchProducts(String keyword) {
        List<Product> products = productRepository.searchByNameOrDescription(keyword, keyword);
        return products.stream()
                .map(product -> new ProductListDTO(
                        product.getId().toHexString(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice().doubleValue(),
                        product.getQuantity(),
                        product.getImage()
                ))
                .collect(Collectors.toList());
    }

    // Validate a product
    private void validateProduct(ProductDTO productDTO) {
        if (productDTO.getName() == null || productDTO.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (productDTO.getPrice() == null || BigDecimal.valueOf(productDTO.getPrice()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0.");
        }

        if (productDTO.getQuantity() == null || productDTO.getQuantity() < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
    }
//    private ProductDTO mapToDTO(Product product) {
//        return new ProductDTO(
//                product.getId(),               // ID
//                product.getName(),             // Name
//                product.getDescription(),      // Description
//                product.getPrice(),            // Price
//                product.getQuantity(),         // Quantity
//                product.getImage(),            // Image
//                product.getCategory(),         // Category
//                product.getComments() != null  // Comments: Map to CommentDTO
//                        ? product.getComments().stream()
//                        .map(comment -> new CommentDTO(
//                                comment.getId(),
//                                comment.getContent(),
//                                comment.getUser()
//                        .collect(Collectors.toList())
//                        : new ArrayList<>()        // Default to an empty list if null
//        );
//    }

}
