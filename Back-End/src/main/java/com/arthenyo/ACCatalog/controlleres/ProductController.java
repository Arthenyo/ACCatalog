package com.arthenyo.ACCatalog.controlleres;

import com.arthenyo.ACCatalog.DTO.CategoryDTO;
import com.arthenyo.ACCatalog.DTO.ProductDTO;
import com.arthenyo.ACCatalog.servicies.CategoryService;
import com.arthenyo.ACCatalog.servicies.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>>findAll(Pageable pageable){
        Page<ProductDTO> page = productService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO>findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO>insert(@RequestBody ProductDTO productDTO){
        productDTO = productService.Insert(productDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(productDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO>update(@PathVariable Long id ,@RequestBody ProductDTO productDTO){
        return ResponseEntity.ok().body(productService.update(id,productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO>delete(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
