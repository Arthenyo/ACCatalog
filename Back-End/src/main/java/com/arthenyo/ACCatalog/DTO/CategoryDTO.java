package com.arthenyo.ACCatalog.DTO;

import com.arthenyo.ACCatalog.entities.Category;
import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {

    private Long id;
    @NotBlank(message = "Campo Obrigatorio")
    private String name;

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category entity) {
        id = entity.getId();
        name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
