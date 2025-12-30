package com.onlinestore.sales_service.feign;

import com.onlinestore.sales_service.dto.ShoppingCartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="shopping-carts-service")
public interface IShoppingCartAPI {

    @GetMapping("/shopping-cart/find/{id_shopping_cart}")
    public ShoppingCartDTO findById(@PathVariable Long id_shopping_cart);
}
