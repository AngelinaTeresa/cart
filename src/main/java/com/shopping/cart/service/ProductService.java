package com.shopping.cart.service;

import com.shopping.cart.exception.ProductServiceException;
import com.shopping.cart.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;

@Component
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;

    String baseUrl = "lb://product/";
    String getQuantityUrl = "products/count/";
    public int getProductCount(int productId)
    {
        String url = baseUrl.concat(getQuantityUrl).concat(String.valueOf(productId));
        HttpHeaders headers = getBaseHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        try{
            ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.GET, entity, Integer.class);
            if(Objects.nonNull(response) && response.getStatusCode().equals(HttpStatus.OK)) {
                return response.getBody();
            }
            else if(Objects.nonNull(response)){
                throw new ProductServiceException(response.getBody().toString(),
                        response.getStatusCode());
            } else{
                throw new ProductServiceException("Unsuccessful response from product service",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
           throw new ProductServiceException("Unsuccessful response from product service",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public HttpHeaders getBaseHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public boolean validProduct(Cart newCart) {
        int productCount = getProductCount(newCart.getProductId());
        return productCount>= newCart.getProductQuantity()?true:false;
    }
}
