package com.winestore.winestore.repository;

import com.winestore.winestore.entity.Product;
import com.winestore.winestore.entity.ProductVariant;
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


  //  @Query(value = "SELECT * FROM products p WHERE " +
//            "LOWER(p.name) LIKE %:query% OR " +
//            "LOWER(p.description) LIKE %:query% OR " +
//            "LOWER(p.type) LIKE %:query%", nativeQuery = true)
//    List<Product> searchByQuery(@Param("query") String query);


//    @Query(value = "SELECT * FROM product p WHERE " +
//            "LOWER(p.name) LIKE %:query%"
//            , nativeQuery = true)
//    List<Product> searchByQuery(@Param("query") String query);

    @Query("""
    SELECT DISTINCT 
        CASE 
            WHEN LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) THEN p.name
            WHEN LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) THEN c.name
        END
    FROM Product p 
    FULL JOIN p.category c 
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) 
       OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
""")
    List<String> getMatchingNamesBySearchQuery(@Param("query") String query);



//thid returns the whole product data
//    @Query("SELECT p FROM Product p LEFT JOIN p.category c " +
//            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
//            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))")
//    @EntityGraph(attributePaths = {"category"})
//    List<Product> searchByQuery(@Param("query") String query);

    @Query("""
    SELECT ps FROM ProductVariant ps
    JOIN ps.product p
    JOIN p.category c
    LEFT JOIN c.parent parent
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(parent.name) LIKE LOWER(CONCAT('%', :query, '%'))
""")
    List<ProductVariant> searchByQuery(@Param("query") String query);


    @Query("SELECT p FROM Product p JOIN p.category c " +
            "WHERE LOWER(c.name) = LOWER(:query) ")
    List<Product> filterByQuery(@Param("query") String query);


    @Query("SELECT ps FROM ProductVariant ps " +
            "JOIN ps.product p " +
            "JOIN p.category c " +
            "WHERE (:categoryName IS NULL OR :categoryName = '' OR LOWER(c.name) = LOWER(:categoryName)) "+
            "ORDER BY ps.sellingPrice DESC")
    Page<ProductVariant> filterProductsDESC(@Param("categoryName") String categoryName,
                                            Pageable pageable);

    @Query("SELECT ps FROM ProductVariant ps " +
            "JOIN ps.product p " +
            "JOIN p.category c " +
            "WHERE (:categoryName IS NULL OR :categoryName = '' OR LOWER(c.name) = LOWER(:categoryName)) "+
            "ORDER BY ps.sellingPrice ASC")
    Page<ProductVariant> filterProductsASc(@Param("categoryName") String categoryName,
                                           Pageable pageable);




}
