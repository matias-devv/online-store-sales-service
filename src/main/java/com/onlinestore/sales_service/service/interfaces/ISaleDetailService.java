package com.onlinestore.sales_service.service.interfaces;

import com.onlinestore.sales_service.dto.SaleDetailDTO;
import com.onlinestore.sales_service.model.SaleDetail;

import java.util.List;

public interface ISaleDetailService {

    public void createSaleDetail(SaleDetail saleDetail);

    public SaleDetailDTO findById(Long sale_detail_id);

    public List<SaleDetailDTO> findAllSaleDetails();
}
