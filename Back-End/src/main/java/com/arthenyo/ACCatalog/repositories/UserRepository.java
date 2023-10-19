package com.arthenyo.ACCatalog.repositories;

import com.arthenyo.ACCatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

}
