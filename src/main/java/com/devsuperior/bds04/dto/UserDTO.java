package com.devsuperior.bds04.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;

import com.devsuperior.bds04.entities.User;



public class UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;
    
    private Long id;
    
    @Email(message =  "Favor entrar um email valido")
    private String email;

    private List<RoleDTO> roles = new ArrayList<>();
  

    public UserDTO(){

    }

    public UserDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public UserDTO(User entity){
        id = entity.getId();
        email = entity.getEmail();
        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

}
