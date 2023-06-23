package com.arthenyo.ACCatalog.controlleres;

import com.arthenyo.ACCatalog.entities.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @GetMapping
    public ResponseEntity<List<Category>>findAll(){
        List<Category>list = new ArrayList<>();
        list.add(new Category(1L,"Books"));
        list.add(new Category(2L,"Informatica"));
        return ResponseEntity.ok().body(list);
    }
}
