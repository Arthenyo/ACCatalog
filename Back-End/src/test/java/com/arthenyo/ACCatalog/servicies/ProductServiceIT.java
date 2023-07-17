package com.arthenyo.ACCatalog.servicies;

import com.arthenyo.ACCatalog.DTO.ProductDTO;
import com.arthenyo.ACCatalog.repositories.ProductRepository;
import com.arthenyo.ACCatalog.servicies.exception.ObjectNotFound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIT {
    @Autowired
    private ProductService service;
    @Autowired
    private ProductRepository repository;
    private long existingId;
    private long nonExistingId;
    private long countTotalProduct;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProduct = 25L;

    }

    @Test
    public void deleteShouldDeleteResourseWhenIdExists(){

        service.delete(existingId);

        Assertions.assertEquals(countTotalProduct - 1, repository.count());
    }

    @Test
    public void deleteShouldthrowObjectNotFoundWhenIdDoesNotExist(){
        Assertions.assertThrows(ObjectNotFound.class,()->{
            service.delete(nonExistingId);
        });
    }

    @Test
    public void findAllshouldReturnPagewhenPage0Size10(){

        PageRequest pageRequest = PageRequest.of(0,10);

        Page<ProductDTO> result = service.findAll(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10,result.getSize());
        Assertions.assertEquals(countTotalProduct, result.getTotalElements());
    }

    @Test
    public void findAllshouldReturnEmptyPagewhenPageDoesNotExist(){

        PageRequest pageRequest = PageRequest.of(50,10);

        Page<ProductDTO> result = service.findAll(pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllshouldReturnOrderedPageWhenSortByName(){

        PageRequest pageRequest = PageRequest.of(0,10, Sort.by("name"));

        Page<ProductDTO> result = service.findAll(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }
}