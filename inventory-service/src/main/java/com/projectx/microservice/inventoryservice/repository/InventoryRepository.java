package com.projectx.microservice.inventoryservice.repository;

import com.projectx.microservice.inventoryservice.entity.InventoryStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface InventoryRepository extends JpaRepository<InventoryStock,Long> {
    @Query(value = "select * from inventory_stock where id=:inventoryId",nativeQuery = true)
    public InventoryStock getById(@Param("inventoryId")Long inventoryId);

    public InventoryStock findInventoryStockByProductId(Long productId);

    public Boolean existsInventoryStockByProductId(Long productId);

    @Modifying
    @Transactional
    @Query(value = "update inventory_stock set remaining_count=:remainingCount "
            +"and reserved_count=:reservedCount where product_id=:productId",nativeQuery = true)
    public Integer updateInventoryStock(@Param("productId")Long productId,
                                        @Param("remainingCount")Integer remainingCount,
                                        @Param("reservedCount")Integer reservedCount);

    @Modifying
    @Transactional
    @Query(value = "update inventory_stock set remaining_count=:remainingCount "
            +"and total_count=:totalCount where product_id=:productId",nativeQuery = true)
    public Integer updateInventoryStockForAdmin(@Param("productId")Long productId,
                                        @Param("remainingCount")Integer remainingCount,
                                        @Param("totalCount")Integer totalCount);
}
