package com.arthenyo.ACCatalog.controlleres;

import com.arthenyo.ACCatalog.DTO.CategoryDTO;
import com.arthenyo.ACCatalog.DTO.ProductDTO;
import com.arthenyo.ACCatalog.servicies.CategoryService;
import com.arthenyo.ACCatalog.servicies.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    @PostMapping
    public ResponseEntity<ProductDTO>insert(@Valid @RequestBody ProductDTO productDTO){
        productDTO = productService.Insert(productDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(productDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(productDTO);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO>update(@PathVariable Long id ,@Valid @RequestBody ProductDTO productDTO){
        return ResponseEntity.ok().body(productService.update(id,productDTO));
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
