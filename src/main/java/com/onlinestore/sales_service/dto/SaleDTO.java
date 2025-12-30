package com.onlinestore.sales_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleDTO {

    private Long id_sale;
    private Long id_user;
    private Long id_shopping_cart;

    private BigDecimal total_price;

    private LocalDate  date;
}
