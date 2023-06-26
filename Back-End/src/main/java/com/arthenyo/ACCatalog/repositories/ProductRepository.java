package com.arthenyo.ACCatalog.repositories;

import com.arthenyo.ACCatalog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
