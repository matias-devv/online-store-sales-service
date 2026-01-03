package com.onlinestore.sales_service.feign;

import com.onlinestore.sales_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="products-service")
public interface IProductAPI {

    @PostMapping("/product/find/by-codes")
    public List<ProductDTO> findProductsByCode(@RequestBody List<Long> codes);
}