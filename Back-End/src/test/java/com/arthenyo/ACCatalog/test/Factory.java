package com.arthenyo.ACCatalog.test;

import com.arthenyo.ACCatalog.DTO.ProductDTO;
import com.arthenyo.ACCatalog.entities.Category;
import com.arthenyo.ACCatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L,"Phone","Good Phone",800.0,"https://img.com/img.png", Instant.parse("2022-06-26T03:00:00Z"));
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product);
    }

    public static Category createCategory(){
        return new Category(2L,"Electronics");
    }
}
