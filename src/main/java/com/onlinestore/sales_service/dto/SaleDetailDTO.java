package com.onlinestore.sales_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Detailed sale information including products")
public class SaleDetailDTO {

    @Schema(description = "Sale detail unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id_sale_detail;

    @Schema(description = "Associated sale ID", example = "1")
    private Long id_sale;

    @Schema(description = "User ID who made the purchase", example = "10")
    private Long id_user;

    @Schema(description = "Shopping cart ID used", example = "5")
    private Long id_shopping_cart;

    @Schema(description = "Sale date", example = "2026-01-04")
    private LocalDate date;

    @Schema(description = "Total price of the sale", example = "299.99")
    private BigDecimal total_price;

    @Schema(description = "List of products purchased in this sale")
    private List<ProductDTO> products_to_take;
}
