package com.arthenyo.ACCatalog.repositories;

import com.arthenyo.ACCatalog.entities.Product;
import com.arthenyo.ACCatalog.test.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

@DataJpaTest
public class ProductyRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private long exintingId;
    private long nonexintingId;
    private long countTotalProduct;

    @BeforeEach
    void setUp() throws Exception{
        exintingId = 1L;
        nonexintingId = 1000L;
        countTotalProduct = 25L;
    }
    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProduct + 1, product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        productRepository.deleteById(exintingId);

        Optional<Product> result = productRepository.findById(exintingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnProductWhendIdExists(){
        Optional<Product> result = productRepository.findById(exintingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnProductleakedWhendIdNonExists(){
        Optional<Product> result = productRepository.findById(nonexintingId);
        Assertions.assertTrue(result.isEmpty());
    }
}
