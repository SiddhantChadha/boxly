package com.siddhant.boxly.helper;

import com.siddhant.boxly.entities.User;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("authHelper")
@EnableMethodSecurity
public class AuthorizationHelper {

    public boolean hasCreatedFile(MethodSecurityExpressionOperations root,Integer fileId){
        User user = (User) root.getAuthentication().getPrincipal();
        return user.getCreatedFiles().stream().filter(file -> Objects.equals(file.getId(), fileId)).count() != 0;
    }

    public boolean hasAccessToFile(MethodSecurityExpressionOperations root,Integer fileId){
        User user = (User) root.getAuthentication().getPrincipal();
        return user.getSharedFiles().stream().filter(file -> Objects.equals(file.getId(), fileId)).count() != 0;
    }

}
