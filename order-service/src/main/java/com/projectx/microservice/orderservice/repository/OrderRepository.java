package com.projectx.microservice.orderservice.repository;

import com.projectx.microservice.orderservice.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<OrderDetails,Long> {

    @Modifying
    @Transactional
    @Query(value = "update order_details set order_status=:orderStatus where id=:orderId",nativeQuery = true)
    Integer updateOrderStatus(@Param("orderId")Long orderId,@Param("orderStatus")String orderStatus);

    Boolean existsOrderDetailsById(Long orderId);
}
