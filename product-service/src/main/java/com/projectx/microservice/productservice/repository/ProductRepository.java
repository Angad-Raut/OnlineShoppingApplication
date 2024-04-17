package com.projectx.microservice.productservice.repository;

import com.projectx.microservice.productservice.entity.ProductDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductDetails,Long> {
    @Query(value = "select * from product_details where product_id=:productId",nativeQuery = true)
    public ProductDetails getById(@Param("productId")Long productId);

    @Query(value = "select * from product_details where category_id=:categoryId",nativeQuery = true)
    public List<ProductDetails> getProductByCategory(@Param("categoryId")Long categoryId);

    Boolean existsByProductName(String productName);
    @Modifying
    @Transactional
    @Query(value = "update product_details set product_status=:status where product_id=:productId",nativeQuery = true)
    Integer updateProductStatusById(@Param("productId")Long productId,@Param("status")Integer status);
}
