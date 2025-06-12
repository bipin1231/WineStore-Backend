package com.winestore.winestore.repository;

import com.winestore.winestore.entity.Product;
import com.winestore.winestore.entity.ProductSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product,Long> {
//

    Optional<Product> findByName(String name);

    @EntityGraph(attributePaths = {"category"})
List<Product> findAll();

  //  @Query(value = "SELECT * FROM products p WHERE " +
//            "LOWER(p.name) LIKE %:query% OR " +
//            "LOWER(p.description) LIKE %:query% OR " +
//            "LOWER(p.type) LIKE %:query%", nativeQuery = true)
//    List<Product> searchByQuery(@Param("query") String query);


//    @Query(value = "SELECT * FROM product p WHERE " +
//            "LOWER(p.name) LIKE %:query%"
//            , nativeQuery = true)
//    List<Product> searchByQuery(@Param("query") String query);


    @Query("SELECT p FROM Product p LEFT JOIN p.category c " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    @EntityGraph(attributePaths = {"category"})
    List<Product> searchByQuery(@Param("query") String query);

    @Query("SELECT p FROM Product p LEFT JOIN p.category c " +
            "WHERE LOWER(c.name) = LOWER(:query) ")
    List<Product> filterByQuery(@Param("query") String query);


    @Query("SELECT ps FROM ProductSize ps " +
            "JOIN ps.product p " +
            "JOIN p.category c " +
            "WHERE (:categoryName IS NULL OR :categoryName = '' OR LOWER(c.name) = LOWER(:categoryName)) "+
            "ORDER BY ps.sellingPrice DESC")
    Page<ProductSize> filterProductsDESC(@Param("categoryName") String categoryName,
                                     Pageable pageable);

    @Query("SELECT ps FROM ProductSize ps " +
            "JOIN ps.product p " +
            "JOIN p.category c " +
            "WHERE (:categoryName IS NULL OR :categoryName = '' OR LOWER(c.name) = LOWER(:categoryName)) "+
            "ORDER BY ps.sellingPrice ASC")
    Page<ProductSize> filterProductsASc(@Param("categoryName") String categoryName,
                                         Pageable pageable);




}
