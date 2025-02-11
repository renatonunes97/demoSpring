package com.example.demo.Dto;

import com.example.demo.Entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserDTO {



    @NotEmpty(message = "Not empty name")
    @Size(min = 2, message = "user name should have at least 2 characters")
    private String name;

    @NotEmpty(message = "Not empty address")
    @Size(min = 5, message = "user name should have at least 2 characters")
    private String address;

    @NotEmpty(message = "Not empty Password")
    private String password;

    @NotEmpty(message = "Not empty Email")
    @Email(message = "Email must be valid")
    private String email;


    public UserDTO() {
    }

    public User mapToEntity(){
        User user = new User();
        user.setName(this.name);
        user.setPassword(this.password);
        user.setEmail(this.email);
        user.setAddress(this.getAddress());
        return user;
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

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
