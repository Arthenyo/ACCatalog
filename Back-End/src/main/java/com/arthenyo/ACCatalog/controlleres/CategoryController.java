package com.arthenyo.ACCatalog.controlleres;

import com.arthenyo.ACCatalog.entities.Category;
import com.arthenyo.ACCatalog.servicies.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<Category>>findAll(Pageable pageable){
        Page<Category> page = categoryService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }
}
