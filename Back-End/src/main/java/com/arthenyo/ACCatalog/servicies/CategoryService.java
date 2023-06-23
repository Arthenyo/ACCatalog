package com.arthenyo.ACCatalog.servicies;

import com.arthenyo.ACCatalog.entities.Category;
import com.arthenyo.ACCatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Category> findAll(Pageable pageable){
        Page<Category> page = categoryRepository.findAll(pageable);
        return page;
    }
}
