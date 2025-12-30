package com.onlinestore.sales_service.service;

import com.onlinestore.sales_service.dto.SaleDTO;

import java.time.LocalDate;
import java.util.List;

public interface ISaleService {

    public String createSale(SaleDTO saleDTO);

    public SaleDTO findBySaleId(Long id);

    public List<SaleDTO> findAllSales();

    public List<SaleDTO> findSalesByDate(LocalDate date);

    public List<SaleDTO> findSalesByUserId(Long userId);
}
