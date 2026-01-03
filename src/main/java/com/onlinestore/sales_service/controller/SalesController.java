package com.onlinestore.sales_service.controller;

import com.onlinestore.sales_service.dto.SaleDTO;
import com.onlinestore.sales_service.dto.SaleDetailDTO;
import com.onlinestore.sales_service.service.interfaces.ISaleDetailService;
import com.onlinestore.sales_service.service.interfaces.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sale")
public class SalesController {

    @Autowired
    private ISaleService iSaleService;

    @Autowired
    private ISaleDetailService iSaleDetailService;

    @PostMapping("/create")
    public String createSale(@RequestBody SaleDTO saleDTO){
        return iSaleService.createSale(saleDTO);
    }

    @GetMapping("/find/{id}")
    public SaleDTO findBySaleId(@PathVariable Long id){
        return iSaleService.findBySaleId(id);
    }

    @GetMapping("/find-all")
    public List<SaleDTO> findAllSales(){
        return iSaleService.findAllSales();
    }

    @GetMapping("/find/date/{date}")
    public List<SaleDTO> findSalesByDate(@PathVariable LocalDate date){
        return iSaleService.findSalesByDate(date);
    }

    @GetMapping("/find/user/{id_user}")
    public List<SaleDTO> findSalesByUser(@PathVariable Long id_user){
        return iSaleService.findSalesByUserId(id_user);
    }

    @GetMapping("/find/sale-detail/{sale_detail_id}")
    public SaleDetailDTO findById(@PathVariable Long sale_detail_id){
        return iSaleDetailService.findById(sale_detail_id);
    }

    @GetMapping("/find-all/sales-details")
    public List<SaleDetailDTO> findAllSaleDetails(){
        return iSaleDetailService.findAllSaleDetails();
    }
}
