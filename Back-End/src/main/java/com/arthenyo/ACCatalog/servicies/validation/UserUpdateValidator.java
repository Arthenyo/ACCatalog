package com.arthenyo.ACCatalog.servicies.validation;

import com.arthenyo.ACCatalog.DTO.UserUpdateDTO;
import com.arthenyo.ACCatalog.customErro.FieldMessage;
import com.arthenyo.ACCatalog.entities.User;
import com.arthenyo.ACCatalog.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        @SuppressWarnings("unchecked")
        var uriVars = (Map<String,String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        long userId = Long.parseLong(uriVars.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        //COLOQUE AQUI SEUS TESTES DE VALIDAÇAO, ACRESCENTANDO OBJETOS FIELDMESSAGE A LISTA
        User user = repository.findByEmail(dto.getEmail());
        if(user != null && userId != user.getId()){
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
