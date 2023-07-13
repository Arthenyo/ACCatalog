package com.arthenyo.ACCatalog.servicies;

import com.arthenyo.ACCatalog.DTO.ProductDTO;
import com.arthenyo.ACCatalog.entities.Category;
import com.arthenyo.ACCatalog.entities.Product;
import com.arthenyo.ACCatalog.repositories.CategoryRepository;
import com.arthenyo.ACCatalog.repositories.ProductRepository;
import com.arthenyo.ACCatalog.servicies.exception.DateBaseException;
import com.arthenyo.ACCatalog.servicies.exception.ObjectNotFound;
import com.arthenyo.ACCatalog.test.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProducyServiceTest {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        product = Factory.createProduct();
        category = Factory.createCategory();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(product));

        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(ObjectNotFound.class);

        Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(ObjectNotFound.class);

        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);

    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(()->{
            productService.delete(existingId);
        });

        Mockito.verify(productRepository,Mockito.times(1)).deleteById(existingId);
    }
    @Test
    public void deleteShouldThrowObjectNotFoundIdDoesNotExist(){
        Assertions.assertThrows(ObjectNotFound.class,()->{
            productService.delete(nonExistingId);
        });
    }
    @Test
    public void deleteShouldThrowDateBaseExceptionDependentId(){
        Assertions.assertThrows(DateBaseException.class,()->{
           productService.delete(dependentId);
        });
    }
    @Test
    public void findAllPageShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = productService.findAll(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public  void findByIdShouldReturnProductDTOWhenIdExists(){
        ProductDTO result = productService.findById(existingId);

        Assertions.assertNotNull(result);

    }

    @Test
    public void findByIdShouldThrowObjectNotFoundIdDoesNotExist(){
        Assertions.assertThrows(ObjectNotFound.class,()->{
            productService.findById(nonExistingId);
        });
    }

    @Test
    public  void updateShouldReturnProductDTOWhenIdExists(){
        ProductDTO result = productService.update(existingId,productDTO);

        Assertions.assertNotNull(result);

    }

    @Test
    public void updateShouldThrowObjectNotFoundIdDoesNotExist(){
        Assertions.assertThrows(ObjectNotFound.class,()->{
            productService.update(nonExistingId,productDTO);
        });
    }


}
