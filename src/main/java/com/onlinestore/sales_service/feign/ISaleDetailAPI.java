package com.onlinestore.sales_service.feign;

import com.onlinestore.sales_service.dto.SaleDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="sales-details-service")
public interface ISaleDetailAPI {

    @PostMapping("sale-detail/create")
    public void createSaleDetail(@RequestBody SaleDetailDTO saleDetailDTO);
}
