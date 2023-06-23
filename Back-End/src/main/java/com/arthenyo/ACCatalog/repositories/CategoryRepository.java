package com.arthenyo.ACCatalog.repositories;

import com.arthenyo.ACCatalog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
