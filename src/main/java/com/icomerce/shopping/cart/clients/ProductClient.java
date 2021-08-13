package com.icomerce.shopping.cart.clients;

import com.icomerce.shopping.cart.payload.response.ProductClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "product-service")
public interface ProductClient {
    @RequestMapping(method = RequestMethod.GET, value = "/product-service/v1/products/{productCode}")
    ProductClientResponse getProductByCode(@PathVariable(value = "productCode") String productCode);
}
