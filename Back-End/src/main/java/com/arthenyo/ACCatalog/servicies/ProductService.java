package com.arthenyo.ACCatalog.servicies;

import com.arthenyo.ACCatalog.DTO.CategoryDTO;
import com.arthenyo.ACCatalog.DTO.ProductDTO;
import com.arthenyo.ACCatalog.entities.Category;
import com.arthenyo.ACCatalog.entities.Product;
import com.arthenyo.ACCatalog.repositories.ProductRepository;
import com.arthenyo.ACCatalog.servicies.exception.DateBaseException;
import com.arthenyo.ACCatalog.servicies.exception.ObjectNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ProductDTO delete(Long id){
        try {
            Product entity = productRepository.getReferenceById(id);
            productRepository.delete(entity);
            return new ProductDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ObjectNotFound("Produto nao encontrada " + id);
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
}
