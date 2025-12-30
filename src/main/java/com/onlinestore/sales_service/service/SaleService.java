package com.onlinestore.sales_service.service;

import com.onlinestore.sales_service.dto.SaleDTO;
import com.onlinestore.sales_service.dto.SaleDetailDTO;
import com.onlinestore.sales_service.dto.ShoppingCartDTO;
import com.onlinestore.sales_service.dto.UserDTO;
import com.onlinestore.sales_service.feign.ISaleDetailAPI;
import com.onlinestore.sales_service.feign.IShoppingCartAPI;
import com.onlinestore.sales_service.feign.IUserAPI;
import com.onlinestore.sales_service.model.Sale;
import com.onlinestore.sales_service.repository.ISaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService implements ISaleService {

    @Autowired
    private ISaleRepository iSaleRepository;

    @Autowired
    private IUserAPI iUserAPI;

    @Autowired
    private IShoppingCartAPI iShoppingCartAPI;

    @Autowired
    private ISaleDetailAPI iSaleDetailAPI;

    @Override
    public String createSale(SaleDTO saleDTO) {

        //I check if the shopping cart belongs to this user
        boolean ok = this.verifyOwnership( saleDTO.getId_user(), saleDTO.getId_shopping_cart());

        //I need to verify if the shopping cart & the user exists because it's crucial
        saleDTO = this.verifyIfUserExists(saleDTO);
        saleDTO = this.verifyIfShoppingCartExists(saleDTO);

        if(saleDTO!= null) {
            //I need to pass the DTO to the entity
            Sale sale = this.convertSaleDTOtoSaleEntity(saleDTO);

            //save
            iSaleRepository.save(sale);

            //I create the sale-detail
            this.createSaleDetail(sale);

            return "The sale was successfully created";
        }
        return "The user or the shopping cart does not exist";
    }

    //circuit breaker;
    private SaleDTO verifyIfShoppingCartExists(SaleDTO saleDTO) {
        //find
        ShoppingCartDTO shoppingCartDTO = iShoppingCartAPI.findById( saleDTO.getId_shopping_cart() );
        if( shoppingCartDTO == null ){
            return null;
        }
        //set
        saleDTO.setId_shopping_cart(shoppingCartDTO.getId_shopping_cart());
        saleDTO.setTotal_price(shoppingCartDTO.getTotal_price());
        return saleDTO;
    }

    //circuit breaker
    private SaleDTO verifyIfUserExists(SaleDTO saleDTO) {
        //find
        UserDTO userDTO = iUserAPI.findByUserId( saleDTO.getId_user() );

        if( userDTO == null ){
            return null;
        }
        //set
        saleDTO.setId_user(userDTO.getId());
        return saleDTO;
    }

    //circuit breaker
    private void createSaleDetail(Sale sale) {
        SaleDetailDTO saleDetailDTO = new SaleDetailDTO();
        saleDetailDTO.setId_sale(sale.getId_sale());
        saleDetailDTO.setId_user(sale.getId_user());
        saleDetailDTO.setId_shopping_cart(sale.getId_shopping_cart());
        saleDetailDTO.setDate(sale.getDate());
        saleDetailDTO.setTotal_price(sale.getTotal_price());
        iSaleDetailAPI.createSaleDetail(saleDetailDTO);
    }

    private boolean verifyOwnership(Long user_id, Long shopping_cart_id) {
        //find
        UserDTO userDTO = iUserAPI.findByUserId( user_id );
        ShoppingCartDTO shoppingCartDTO = iShoppingCartAPI.findById( shopping_cart_id);

        List<Long> idsShoppingCartsFromUser = userDTO.getIds_shopping_cart();

        for( Long id : idsShoppingCartsFromUser){

            //This means that the shopping cart belongs to the user
            if( id.equals( shoppingCartDTO.getId_shopping_cart() ) ){
                return true;
            }
        }
        return false;
    }

    private Sale convertSaleDTOtoSaleEntity(SaleDTO saleDTO) {
        Sale sale = new Sale();
        sale.setId_user( saleDTO.getId_user() );
        sale.setId_shopping_cart( saleDTO.getId_shopping_cart() );
        sale.setTotal_price( saleDTO.getTotal_price() );
        sale.setDate( saleDTO.getDate() );
        return sale;
    }

    @Override
    public SaleDTO findBySaleId(Long id) {
        Sale sale = iSaleRepository.findById(id).orElse(null);
        return this.createDTO(sale);
    }

    private SaleDTO createDTO(Sale sale) {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId_sale(sale.getId_sale());
        saleDTO.setId_user(sale.getId_user());
        saleDTO.setId_shopping_cart(sale.getId_shopping_cart());
        saleDTO.setDate(sale.getDate());
        saleDTO.setTotal_price(sale.getTotal_price());
        return saleDTO;
    }

    @Override
    public List<SaleDTO> findAllSales() {
        List<Sale> listSales = iSaleRepository.findAll();
        List<SaleDTO> saleDTOList = new ArrayList<>();

        if( listSales == null){
            return null;
        }
        for( Sale sale : listSales ){
            saleDTOList.add( this.createDTO(sale) );
        }
        return saleDTOList;
    }

    @Override
    public List<SaleDTO> findSalesByDate(LocalDate date) {
        List<Sale> listSales = iSaleRepository.findAll();
        List<SaleDTO> saleDTOList = new ArrayList<>();

        if( listSales == null){
            return null;
        }
        //If it's the same date, I'll convert the sale into a discount.
        for( Sale sale : listSales ){
            if( sale.getDate().equals(date) ){
                saleDTOList.add( this.createDTO(sale) );
            }
        }
        return saleDTOList;
    }

    @Override
    public List<SaleDTO> findSalesByUserId(Long userId) {
        List<Sale> listSales = iSaleRepository.findAll();
        List<SaleDTO> saleDTOList = new ArrayList<>();

        if( listSales == null){
            return null;
        }
        //If it's the same date, I'll convert the sale into a discount.
        for( Sale sale : listSales ){
            if( sale.getId_user().equals(userId) ){
                saleDTOList.add( this.createDTO(sale) );
            }
        }
        return saleDTOList;
    }
}
