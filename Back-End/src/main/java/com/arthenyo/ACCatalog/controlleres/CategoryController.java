package com.arthenyo.ACCatalog.controlleres;

import com.arthenyo.ACCatalog.DTO.CategoryDTO;
import com.arthenyo.ACCatalog.servicies.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>>findAll(Pageable pageable){
        Page<CategoryDTO> page = categoryService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO>findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO>insert(@Valid @RequestBody CategoryDTO categoryDTO){
        categoryDTO = categoryService.Insert(categoryDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(categoryDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO>update(@PathVariable Long id ,@Valid @RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.ok().body(categoryService.update(id,categoryDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
