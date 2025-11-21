package org.ismail.mouvementstock.client;

import org.ismail.mouvementstock.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;

    @Value("${product.service.url:http://localhost:8085/api/v1/products}")
    private String productServiceUrl;

    public ProductClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductDTO getProductById(Long id) {
        String url = productServiceUrl + "/" + id;
        return restTemplate.getForObject(url, ProductDTO.class);
    }

    public void updateProductQuantity(Long id, Integer newQuantity) {
        String url = productServiceUrl + "/" + id + "/quantity";
        restTemplate.put(url, newQuantity);
    }

    public boolean productExists(Long id) {
        try {
            getProductById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

