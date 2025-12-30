package com.onlinestore.sales_service.controller;

import com.onlinestore.sales_service.dto.SaleDTO;
import com.onlinestore.sales_service.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sale")
public class SalesController {

    @Autowired
    private ISaleService iSaleService;

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

    @GetMapping("/find/{date}")
    public List<SaleDTO> findSalesByDate(LocalDate date){
        return iSaleService.findSalesByDate(date);
    }

    @GetMapping("/find/{id_user}")
    public List<SaleDTO> findSalesByDate(Long id_user){
        return iSaleService.findSalesByUserId(id_user);
    }
}
