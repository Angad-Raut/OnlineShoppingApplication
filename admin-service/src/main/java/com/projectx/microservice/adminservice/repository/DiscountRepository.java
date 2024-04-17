package com.projectx.microservice.adminservice.repository;

import com.projectx.microservice.adminservice.entity.DiscountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountDetails,Long> {
    @Query(value = "select * from discount_details where id=:discountId",nativeQuery = true)
    DiscountDetails getById(@Param("discountId")Long discountId);

    Boolean existsDiscountDetailsByDiscountTypeAndDiscount(Integer discountType,Double discount);

    @Modifying
    @Transactional
    @Query(value = "update discount_details set status=:status where id=:discountId",nativeQuery = true)
    Integer updateStatus(@Param("discountId")Long discountId,@Param("status")Boolean status);

    @Query(value = "select discount from discount_details where discount_type=:discountType",nativeQuery = true)
    List<Double> getDiscountByType(@Param("discountType")Integer discountType);
}
