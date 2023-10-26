package com.arthenyo.ACCatalog.servicies;

import com.arthenyo.ACCatalog.DTO.CategoryDTO;
import com.arthenyo.ACCatalog.DTO.ProductDTO;
import com.arthenyo.ACCatalog.entities.Category;
import com.arthenyo.ACCatalog.entities.Product;
import com.arthenyo.ACCatalog.projections.ProductProjection;
import com.arthenyo.ACCatalog.repositories.ProductRepository;
import com.arthenyo.ACCatalog.servicies.exception.DateBaseException;
import com.arthenyo.ACCatalog.servicies.exception.ObjectNotFound;
import com.arthenyo.ACCatalog.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){
        Page<Product> page = productRepository.findAll(pageable);
        return page.map(x -> new ProductDTO(x));
    }
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFound("Produto não encontrada" + id));
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO Insert(ProductDTO productDTO){
        Product entity = new Product();
        entityToDto(entity,productDTO);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO){
        try {
            Product entity = productRepository.getReferenceById(id);
            entityToDto(entity,productDTO);
            entity = productRepository.save(entity);
            return new ProductDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ObjectNotFound("Produto nao encontrada " + id);
        }
    }

    @Transactional
    public void delete(Long id){
        if(!productRepository.existsById(id)){
            throw new ObjectNotFound("Produto nao encontrada " + id);
        }
        try {
            productRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DateBaseException("Não foi possivel deletar a Produto %d, erro de integridade " + id);
        }
    }


    private void entityToDto(Product entity, ProductDTO dto){
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.getCategories().clear();
        for(CategoryDTO categoryDTO : dto.getCategories()){
            Category category = new Category();
            category.setId(categoryDTO.getId());
            entity.getCategories().add(category);
        }
    }
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPage(String name, String categoryId, Pageable pageable) {
        List<Long> categotyIds = Arrays.asList();
        if(!"0".equals(categoryId)){
            categotyIds = Arrays.asList(categoryId.split(",")).stream().map(Long::parseLong).toList();
        }
        Page<ProductProjection> page = productRepository.searchProduct(name, categotyIds,pageable);
        List<Long> productIds = page.map(ProductProjection::getId).toList();

        List<Product> entities = productRepository.serchProductsWithCategory(productIds);

        entities = (List<Product>) Utils.replace(page.getContent(),entities);

        List<ProductDTO> dtos = entities.stream().map(ProductDTO::new).toList();

        Page<ProductDTO> pageDto = new PageImpl<>(dtos,page.getPageable(),page.getTotalElements());
        return pageDto;
    }
}
