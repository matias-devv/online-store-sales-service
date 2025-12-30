package com.onlinestore.sales_service.feign;

import com.onlinestore.sales_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="users-service")
public interface IUserAPI {

    @GetMapping("user/find/{id}")
    public UserDTO findByUserId(@PathVariable Long id);
}
