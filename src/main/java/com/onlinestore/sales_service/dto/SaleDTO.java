package com.onlinestore.sales_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Sale information")
public class SaleDTO {

    @Schema(description = "Sale unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id_sale;

    @Schema(description = "User ID who made the purchase", example = "10")
    private Long id_user;

    @Schema(description = "Shopping cart ID to convert into sale", example = "5")
    private Long id_shopping_cart;

    @Schema(description = "Sale date", example = "2026-01-04")
    private LocalDate date;

    @Schema(description = "Total price (auto-calculated from shopping cart)", example = "299.99", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal total_price;

}
