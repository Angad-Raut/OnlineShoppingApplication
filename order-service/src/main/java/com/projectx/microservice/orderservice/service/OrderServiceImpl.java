package com.projectx.microservice.orderservice.service;

import com.projectx.microservice.exceptions.ProductNotVerifiedException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.orderservice.entity.OrderDetails;
import com.projectx.microservice.orderservice.entity.OrderItems;
import com.projectx.microservice.orderservice.feignconfig.InventoryStockFeignClient;
import com.projectx.microservice.orderservice.feignconfig.PaymentFeignClient;
import com.projectx.microservice.orderservice.feignconfig.ProductFeignClient;
import com.projectx.microservice.orderservice.payloads.OrderDto;
import com.projectx.microservice.orderservice.payloads.OrderItemDto;
import com.projectx.microservice.orderservice.repository.OrderRepository;
import com.projectx.microservice.payloads.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentFeignClient paymentFeignClient;

    @Autowired
    private InventoryStockFeignClient inventoryStockFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    private static final String ORDER_CREATED="Created";
    private static final String ORDER_COMPLETED="Completed";

    public Double totalTaxableAmount = 0.0;

    @Override
    public Boolean placeOrder(OrderDto dto) {
        try {
            Set<OrderItems> orderItemsSet = setOrderItems(dto.getItemDtoSet());
            PaymentAmountDto paymentAmountDto = new PaymentAmountDto(totalTaxableAmount);
            ResponseEntity<ResponseDto<Long>> response = paymentFeignClient.savePaymentDetails(paymentAmountDto);
            Long paymentId = response.getBody().getResult();
            OrderDetails orderDetails = orderRepository.save(OrderDetails.builder()
                    .customerId(dto.getCustomerId())
                    .orderDate(new Date())
                    .orderAmount(dto.getOrderAmount())
                    .orderStatus(ORDER_CREATED)
                    .orderTaxableAmount(totalTaxableAmount)
                    .discountType(dto.getDiscountType())
                    .discountAmount(dto.getDiscountAmount())
                    .paymentId(paymentId)
                    .orderItemsSet(orderItemsSet)
                    .build());
            return orderDetails!=null?updateInventoryStock(orderDetails.getOrderItemsSet()):false;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Boolean updateOrderStatus(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            Integer count = null;
            if (orderRepository.existsOrderDetailsById(dto.getEntityId())) {
                count = orderRepository.updateOrderStatus(dto.getEntityId(),ORDER_COMPLETED);
            } else {
                throw new ResourceNotFoundException("Order details not present in system!!");
            }
            return count==1?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    private Set<OrderItems> setOrderItems(Set<OrderItemDto> itemDtoList) {
        Set<OrderItems> orderItemsList = new HashSet<>();
        for (OrderItemDto dto:itemDtoList) {
            EntityIdAndTypeDto entityIdAndTypeDto = new EntityIdAndTypeDto(dto.getProductId(),dto.getProductQuantity());
            ResponseEntity<ResponseDto<Boolean>> stockResponse = inventoryStockFeignClient.isProductInStock(entityIdAndTypeDto);
            if (stockResponse.getBody().getResult()!=null && stockResponse.getBody().getResult()==true) {
                ResponseEntity<ResponseDto<ProductInfoDto>> ProductInfoResponse = productFeignClient.getProductInfoByProduct(new EntityIdDto(dto.getProductId()));
                ProductInfoDto productInfoDto = ProductInfoResponse.getBody().getResult();
                Double productPrice = productInfoDto.getProductPrice();
                Integer taxPercentage = productInfoDto.getProductTaxPercentage();
                Double totalPerProduct = (productPrice*dto.getProductQuantity());
                Double totalTaxablePerProduct = ((totalPerProduct*taxPercentage)/100);
                totalTaxableAmount = (totalTaxableAmount+(totalTaxablePerProduct*2));
                OrderItems orderItems = OrderItems.builder()
                        .productId(dto.getProductId())
                        .productPrice(productPrice)
                        .productQuantity(dto.getProductQuantity())
                        .totalPrice(totalPerProduct)
                        .cgstPercentage(taxPercentage)
                        .cgstTaxAmount(totalTaxablePerProduct)
                        .sgstPercentage(taxPercentage)
                        .sgstTaxAmount(totalTaxablePerProduct)
                        .igstPercentage(taxPercentage)
                        .igstTaxAmount(totalTaxablePerProduct)
                        .build();
                orderItemsList.add(orderItems);
            } else {
                throw new ProductNotVerifiedException("Product is not verified!!");
            }
        }
        return orderItemsList;
    }
    private Boolean updateInventoryStock(Set<OrderItems> orderItems) {
        Integer count = 0;
        for (OrderItems items : orderItems) {
            EntityIdAndTypeDto requestDto = new EntityIdAndTypeDto(items.getProductId(),items.getProductQuantity());
            ResponseEntity<ResponseDto<Boolean>>  response = inventoryStockFeignClient.updateStockQuantity(requestDto);
            if (response.getBody().getResult()) {
                count++;
            }
        }
        return count.equals(orderItems.size())?true:false;
    }
}
