package com.onlinestore.sales_service.service.implementation;

import com.onlinestore.sales_service.dto.SaleDTO;
import com.onlinestore.sales_service.dto.SaleDetailDTO;
import com.onlinestore.sales_service.dto.ShoppingCartDTO;
import com.onlinestore.sales_service.dto.UserDTO;
import com.onlinestore.sales_service.feign.IShoppingCartAPI;
import com.onlinestore.sales_service.feign.IUserAPI;
import com.onlinestore.sales_service.model.Sale;
import com.onlinestore.sales_service.model.SaleDetail;
import com.onlinestore.sales_service.repository.ISaleRepository;
import com.onlinestore.sales_service.service.interfaces.ISaleDetailService;
import com.onlinestore.sales_service.service.interfaces.ISaleService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
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
    private ISaleDetailService iSaleDetailService;

    @Override
    public String createSale(SaleDTO saleDTO) {

        //I check if the shopping cart belongs to this user
        boolean ok = this.verifyOwnership( saleDTO.getId_user(), saleDTO.getId_shopping_cart() );

        //I need to verify if the shopping cart & the user exists because it's crucial
        saleDTO = this.verifyIfUserExists(saleDTO);
        saleDTO = this.verifyIfShoppingCartExists(saleDTO);

        if(!ok){
            return "This user doesn't have any shopping cart asigned";
        }
        if( saleDTO == null ) {
            return "The user or the shopping cart does not exist";
        }
        //I need to pass the DTO to the entity
        Sale sale = this.convertSaleDTOtoSaleEntity(saleDTO);

        iSaleRepository.save(sale);

        this.createSaleDetail(sale);

        return "The sale was successfully created";
    }

    @CircuitBreaker( name = "shopping-carts-service", fallbackMethod = "fallbackVerifyIfShoppingCartExists")
    @Retry( name = "shopping-carts-service")
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

    public SaleDTO fallbackVerifyIfShoppingCartExists(Throwable throwable){
        return new SaleDTO(111111L,111111L,111111L, null,null);
    }

    @CircuitBreaker(name="users-service", fallbackMethod = "fallbackVerifyIfUserExist")
    @Retry(name="users-service")
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

    public SaleDTO fallbackVerifyIfUserExist(Throwable throwable) {
        return new SaleDTO(9999999L,999999L,999999L, null,null);
    }

    private void createSaleDetail(Sale sale) {
        SaleDetail saleDetail = new SaleDetail();

        saleDetail.setId_sale(sale.getId_sale());
        saleDetail.setId_user(sale.getId_user());
        saleDetail.setId_shopping_cart(sale.getId_shopping_cart());
        saleDetail.setTotal_price(sale.getTotal_price());
        saleDetail.setDate(sale.getDate());
        //llamar a saleDetailservice
        iSaleDetailService.createSaleDetail(saleDetail);
    }

    public void fallbackCreateSaleDetail(Throwable throwable) throws ServiceUnavailableException {
        throw new ServiceUnavailableException("sales-details-service unavailable. method: createSaleDetail");
    }

    private boolean verifyOwnership(Long user_id, Long shopping_cart_id) {
        //find
        UserDTO userDTO = iUserAPI.findByUserId( user_id );
        ShoppingCartDTO shoppingCartDTO = iShoppingCartAPI.findById( shopping_cart_id);

        List<Long> idsShoppingCartsFromUser = userDTO.getIds_shopping_cart();

        if ( idsShoppingCartsFromUser == null ) {
            return false;
        }
        if( shoppingCartDTO.getId_user() != user_id ) {
            return false;
        }
        return true;
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
