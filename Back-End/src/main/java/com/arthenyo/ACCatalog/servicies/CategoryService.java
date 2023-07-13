package com.arthenyo.ACCatalog.servicies;

import com.arthenyo.ACCatalog.DTO.CategoryDTO;
import com.arthenyo.ACCatalog.entities.Category;
import com.arthenyo.ACCatalog.repositories.CategoryRepository;
import com.arthenyo.ACCatalog.servicies.exception.DateBaseException;
import com.arthenyo.ACCatalog.servicies.exception.ObjectNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.h2.message.DbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable){
        Page<Category> page = categoryRepository.findAll(pageable);
        return page.map(x -> new CategoryDTO(x));
    }
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFound("Categoria não encontrada" + id));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO Insert(CategoryDTO categoryDTO){
        Category entity = new Category();
        entityToDto(entity,categoryDTO);
        entity = categoryRepository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO){
        try {
            Category entity = categoryRepository.getReferenceById(id);
            entityToDto(entity,categoryDTO);
            entity = categoryRepository.save(entity);
            return new CategoryDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ObjectNotFound("Categoria nao encontrada " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if(!categoryRepository.existsById(id)){
            throw new ObjectNotFound("Categoria nao encontrada " + id);
        }
        try {
            categoryRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DateBaseException("Não foi possivel deletar a Categoria " + id + ", erro de integridade");
        }
    }

    private void entityToDto(Category entity, CategoryDTO dto){
        entity.setName(dto.getName());
    }
}
