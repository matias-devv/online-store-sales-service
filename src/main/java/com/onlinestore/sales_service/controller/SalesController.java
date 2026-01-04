package com.onlinestore.sales_service.controller;

import com.onlinestore.sales_service.dto.SaleDTO;
import com.onlinestore.sales_service.dto.SaleDetailDTO;
import com.onlinestore.sales_service.service.interfaces.ISaleDetailService;
import com.onlinestore.sales_service.service.interfaces.ISaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sale")
@Tag(name = "Sales", description = "Sales management API")
public class SalesController {

    @Autowired
    private ISaleService iSaleService;

    @Autowired
    private ISaleDetailService iSaleDetailService;

    @Operation(
            summary = "Create a new sale",
            description = "Creates a sale from a shopping cart and generates the sale detail with products. " +
                    "Validates user ownership of the shopping cart and verifies existence of both user and cart."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale created successfully or validation error message",
                    content = @Content(schema = @Schema(implementation = String.class,
                            example = "The sale was successfully created")))
    })
    @PostMapping("/create")
    public String createSale(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Requires: user ID, shopping cart ID, and date",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SaleDTO.class))
            )
            @RequestBody SaleDTO saleDTO) {
        return iSaleService.createSale(saleDTO);
    }


    @Operation(
            summary = "Get sale by ID",
            description = "Retrieves detailed information of a specific sale"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale found",
                    content = @Content(schema = @Schema(implementation = SaleDTO.class))),
            @ApiResponse(responseCode = "404", description = "Sale not found")
    })
    @GetMapping("/find/{id}")
    public SaleDTO findBySaleId(@PathVariable Long id){
        return iSaleService.findBySaleId(id);
    }


    @Operation(
            summary = "Get all sales",
            description = "Retrieves a list of all sales in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sales list retrieved successfully",
                    content = @Content(schema = @Schema(implementation = SaleDTO.class)))
    })
    @GetMapping("/find-all")
    public List<SaleDTO> findAllSales(){
        return iSaleService.findAllSales();
    }


    @Operation(
            summary = "Get sales by date",
            description = "Retrieves all sales made on a specific date"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sales found for the specified date",
                    content = @Content(schema = @Schema(implementation = SaleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date format")
    })
    @GetMapping("/find/date/{date}")
    public List<SaleDTO> findSalesByDate(
                                             @Parameter(description = "Date in ISO format (YYYY-MM-DD)", required = true, example = "2026-01-04")
                                             @PathVariable LocalDate date){
        return iSaleService.findSalesByDate(date);
    }

    @Operation(
            summary = "Get sales by user",
            description = "Retrieves all sales made by a specific user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sales found for the specified user",
                    content = @Content(schema = @Schema(implementation = SaleDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/find/user/{id_user}")
    public List<SaleDTO> findSalesByUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id_user) {
        return iSaleService.findSalesByUserId(id_user);
    }

    @Operation(
            summary = "Get sale detail by ID",
            description = "Retrieves detailed information of a specific sale including product details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale detail found",
                    content = @Content(schema = @Schema(implementation = SaleDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "Sale detail not found")
    })
    @GetMapping("/find/sale-detail/{sale_detail_id}")
    public SaleDetailDTO findById(
            @Parameter(description = "Sale detail ID", required = true, example = "1")
            @PathVariable Long sale_detail_id) {
        return iSaleDetailService.findById(sale_detail_id);
    }

    @Operation(
            summary = "Get all sale details",
            description = "Retrieves a list of all sale details in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale details list retrieved successfully",
                    content = @Content(schema = @Schema(implementation = SaleDetailDTO.class)))
    })
    @GetMapping("/find-all/sales-details")
    public List<SaleDetailDTO> findAllSaleDetails(){
        return iSaleDetailService.findAllSaleDetails();
    }
}
