package com.projectx.microservice.apigateway.filterconfig;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/auth/login",
            "/auth/refreshToken",
            "/auth/logout",
            "/auth/registerUser",
            "/auth/getUserDetailsById",
            "/auth/getAllUsers",
            "/auth/getUserInfoByMobile",
            "/api/category/getAllProductCategories",
            "/api/category/addCategory",
            "/api/category/getByIdCategory",
            "/api/category/getCategoryDropDown",
            "/api/category/getProductCategory",
            "/api/inventoryStock/addStock",
            "/api/inventoryStock/getInventoryStock",
            "/api/inventoryStock/getAllProductsInventoryStock",
            "/api/inventoryStock/isProductInStock",
            "/api/inventoryStock/updateStockQuantity",
            "/api/inventoryStock/updateStockQuantityForAdmin",
            "/api/inventoryStock/isInventoryExist",
            "/api/product/addProduct",
            "/api/product/getProductById",
            "/api/product/updateProductStatusById",
            "/api/product/getProductDropDown",
            "/api/product/getAllProductList",
            "/api/product/getProductDetails",
            "/api/product/isVerifiedProduct",
            "/api/product/getProductInfoByProduct",
            "/api/product/getProductDropDownByCategory",
            "/api/orders/placeOrder",
            "/api/orders/updateOrderStatus",
            "/api/payment/savePaymentDetails",
            "/api/payment/doOfflinePayment",
            "/api/payment/doOnlinePayment",
            "/api/discount/addDiscount",
            "/api/discount/getDiscount",
            "/api/discount/updateStatus",
            "/api/discount/getDiscountDropDown",
            "/api/discount/getAllDiscounts"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
