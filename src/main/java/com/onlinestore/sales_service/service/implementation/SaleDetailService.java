package com.onlinestore.sales_service.service.implementation;

import com.onlinestore.sales_service.dto.ProductDTO;
import com.onlinestore.sales_service.dto.SaleDetailDTO;
import com.onlinestore.sales_service.dto.ShoppingCartDTO;
import com.onlinestore.sales_service.feign.IProductAPI;
import com.onlinestore.sales_service.feign.IShoppingCartAPI;
import com.onlinestore.sales_service.model.SaleDetail;
import com.onlinestore.sales_service.repository.ISaleDetailRepository;
import com.onlinestore.sales_service.service.interfaces.ISaleDetailService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleDetailService implements ISaleDetailService {


    @Autowired
    private ISaleDetailRepository iSaleDetailRepository;

    @Autowired
    private IShoppingCartAPI iShoppingCartAPI;

    @Autowired
    private IProductAPI iProductAPI;

    @Override
    public void createSaleDetail(SaleDetail saleDetail) {

        //I'm looking for the shopping cart by ID
        //Once I have the list of products ->  I set it to "saleDetail"
        ShoppingCartDTO shoppingCart = this.findShoppingCartById( saleDetail.getId_shopping_cart() );

        List<Long> product_codes = this.getProductCodesFromShoppingCart(shoppingCart);

        List<ProductDTO> productsFound = this.findProductsByCodes( product_codes );

        productsFound = this.setQuantityToTake(productsFound, shoppingCart);

        saleDetail.setProducts_to_take(productsFound);

        iSaleDetailRepository.save(saleDetail);

    }

    private List<ProductDTO> setQuantityToTake(List<ProductDTO> productsFound, ShoppingCartDTO shoppingCart) {

        List<ProductDTO> listShoppingCart = shoppingCart.getProducts();

        for ( ProductDTO productFromShoppingCart : listShoppingCart) {
            for( ProductDTO product : productsFound){

                if ( productFromShoppingCart.getCode().equals( product.getCode() )){
                    product.setQuantity(productFromShoppingCart.getQuantity());
                }
            }
        }
        return productsFound;
    }

    private List<Long> getProductCodesFromShoppingCart(ShoppingCartDTO shoppingCart) {

        List<ProductDTO> products = shoppingCart.getProducts();
        List<Long> product_codes = new ArrayList<>();

        for(ProductDTO pr : products){
            product_codes.add(pr.getCode());
        }
        return product_codes;
    }

    @CircuitBreaker( name = "products-service", fallbackMethod = "fallbackFindProductsByCodes")
    @Retry(name="products-service")
    private List<ProductDTO> findProductsByCodes(List<Long> productCodes) {
        return iProductAPI.findProductsByCode(productCodes);
    }

    public List<ProductDTO> fallbackFindProductsByCodes(Throwable throwable) throws ServiceUnavailableException {
        throw new ServiceUnavailableException("the service products is unavaible");
    }

    @CircuitBreaker( name = "shopping-carts-service", fallbackMethod = "fallbackFindShoppingCartById")
    @Retry(name="shopping-carts-service")
    private ShoppingCartDTO findShoppingCartById(Long idShoppingCart) {
        return iShoppingCartAPI.findById( idShoppingCart );
    }

    public ShoppingCartDTO  fallbackFindShoppingCartById(Throwable throwable) throws ServiceUnavailableException {
        throw new ServiceUnavailableException("the service shopping-carts is unavaible");
    }

    @Override
    public SaleDetailDTO findById(Long sale_detail_id) {
        SaleDetail saleDetail = iSaleDetailRepository.findById(sale_detail_id).orElse(null);
        return this.convertSaleDetailToDTO(saleDetail);
    }

    @Override
    public List<SaleDetailDTO> findAllSaleDetails() {
        List<SaleDetail> saleDetails = iSaleDetailRepository.findAll();
        List<SaleDetailDTO> saleDetailDTOs = new ArrayList<>();

        for(SaleDetail saleDetail : saleDetails){
            saleDetailDTOs.add( convertSaleDetailToDTO(saleDetail) );
        }
        return saleDetailDTOs;
    }

    private SaleDetailDTO convertSaleDetailToDTO(SaleDetail saleDetail) {
        SaleDetailDTO saleDetailDTO = new SaleDetailDTO();
        saleDetailDTO.setId_sale(saleDetail.getId_sale());
        saleDetailDTO.setId_user(saleDetail.getId_user());
        saleDetailDTO.setTotal_price(saleDetail.getTotal_price());
        saleDetailDTO.setId_shopping_cart(saleDetail.getId_shopping_cart());
        saleDetailDTO.setDate(saleDetail.getDate());
        return saleDetailDTO;
    }
}
