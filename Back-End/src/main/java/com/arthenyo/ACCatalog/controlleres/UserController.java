package com.arthenyo.ACCatalog.controlleres;

import com.arthenyo.ACCatalog.DTO.ProductDTO;
import com.arthenyo.ACCatalog.DTO.UserDTO;
import com.arthenyo.ACCatalog.DTO.UserInsertDTO;
import com.arthenyo.ACCatalog.DTO.UserUpdateDTO;
import com.arthenyo.ACCatalog.servicies.ProductService;
import com.arthenyo.ACCatalog.servicies.UserService;
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
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>>findAll(Pageable pageable){
        Page<UserDTO> page = userService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO>findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO>insert(@Valid @RequestBody UserInsertDTO userDTO){
        UserDTO newDTO = userService.Insert(userDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(newDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(newDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO>update(@PathVariable Long id , @Valid @RequestBody UserUpdateDTO userUpdateDTO){
        UserDTO newDto = userService.update(id,userUpdateDTO);
        return ResponseEntity.ok().body(newDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
