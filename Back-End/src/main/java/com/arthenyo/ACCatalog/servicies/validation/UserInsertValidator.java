package com.arthenyo.ACCatalog.servicies.validation;

import com.arthenyo.ACCatalog.DTO.UserInsertDTO;
import com.arthenyo.ACCatalog.customErro.FieldMessage;
import com.arthenyo.ACCatalog.entities.User;
import com.arthenyo.ACCatalog.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        //COLOQUE AQUI SEUS TESTES DE VALIDAÇAO, ACRESCENTANDO OBJETOS FIELDMESSAGE A LISTA
        User user = repository.findByEmail(dto.getEmail());
        if(user != null){
            list.add(new FieldMessage("email", "Email ja existe"));
        }
        for(FieldMessage e : list){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
