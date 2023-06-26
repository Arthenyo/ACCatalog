package com.arthenyo.ACCatalog.servicies;

import com.arthenyo.ACCatalog.DTO.CategoryDTO;
import com.arthenyo.ACCatalog.entities.Category;
import com.arthenyo.ACCatalog.repositories.CategoryRepository;
import com.arthenyo.ACCatalog.servicies.exception.ObjectNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new ObjectNotFound("Categoria não encontrada"));
        return new CategoryDTO(category);
    }
}
