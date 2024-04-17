package com.projectx.microservice.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product_details")
public class ProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String productBrand;
    private String productDescription;
    private Double productPrice;
    private String productHsnCode;
    private Integer productTaxPercentage;
    private Long categoryId;
    @ElementCollection
    @CollectionTable(name = "product_features", joinColumns = @JoinColumn(name = "product_id", nullable = false))
    private List<ProductFeatures> productFeatures = new ArrayList<>();
    @Lob
    private byte[] productImage;
    private Integer productStatus;
    private Date insertedTime;
    private Date updatedTime;
}
