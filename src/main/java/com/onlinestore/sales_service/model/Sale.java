package com.onlinestore.sales_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @AllArgsConstructor @NoArgsConstructor @Getter
@Setter
public class Sale {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_sale;

    private Long id_user;
    private Long id_shopping_cart;

    private BigDecimal total_price;
    private LocalDate  date;
}
