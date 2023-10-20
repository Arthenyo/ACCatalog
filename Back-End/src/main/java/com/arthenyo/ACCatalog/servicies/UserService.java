package com.arthenyo.ACCatalog.servicies;

import com.arthenyo.ACCatalog.DTO.*;
import com.arthenyo.ACCatalog.entities.Role;
import com.arthenyo.ACCatalog.entities.User;
import com.arthenyo.ACCatalog.projections.UserDetailsProjection;
import com.arthenyo.ACCatalog.repositories.UserRepository;
import com.arthenyo.ACCatalog.servicies.exception.DateBaseException;
import com.arthenyo.ACCatalog.servicies.exception.ObjectNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable){
        Page<User> page = userRepository.findAll(pageable);
        return page.map(x -> new UserDTO(x));
    }
    @Transactional(readOnly = true)
    public UserDTO findById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFound("Usuario não encontrada" + id));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO Insert(UserInsertDTO userDTO){
        User entity = new User();
        entityToDto(entity,userDTO);
        entity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO userDTO){
        try {
            User entity = userRepository.getReferenceById(id);
            entityToDto(entity,userDTO);
            entity = userRepository.save(entity);
            return new UserDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ObjectNotFound("Usuario nao encontrada " + id);
        }
    }

    @Transactional
    public void delete(Long id){
        if(!userRepository.existsById(id)){
            throw new ObjectNotFound("Usuario nao encontrada " + id);
        }
        try {
            userRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DateBaseException("Não foi possivel deletar o Usuario %d, erro de integridade " + id);
        }
    }


    private void entityToDto(User entity, UserDTO dto){
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.getRoles().clear();
        for(RoleDTO roleDTO : dto.getRoles()){
            Role role = new Role();
            role.setId(roleDTO.getId());
            entity.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
        if(result.size() == 0){
            throw new UsernameNotFoundException("Usuario nao encontrado");
        }
        User user = new User();
        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());
        for(UserDetailsProjection projection : result){
            user.addRole(new Role(projection.getRoleId(),projection.getAuthority()));
        }
        return user;
    }
}
