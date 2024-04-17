package com.projectx.microservice.categoryservice.repository;

import com.projectx.microservice.categoryservice.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Long> {
    @Query(value = "select * from product_category where id=:categoryId",nativeQuery = true)
    ProductCategory getById(@Param("categoryId")Long categoryId);

    Boolean existsByCategoryName(String categoryName);
}
