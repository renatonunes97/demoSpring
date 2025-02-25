package com.example.demo.Entity;

import com.example.demo.Dto.UserDTO;
import com.example.demo.Security.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    private String address;
    private String password;
    private String email;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Task> tasks;


    private String roles;

    public User() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
       if(this.roles == null || roles.isEmpty()){
           return "UNKNOWN";
       }
       try {
           Roles roles = Roles.valueOf(this.roles.toUpperCase());
           return roles.getRole();

       }catch (IllegalArgumentException e) {
           return roles;
       }
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
