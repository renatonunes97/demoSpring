package com.example.demo.Controller;

import com.example.demo.Dto.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ControllerDemo {

    @Autowired
    private UserService userService;



    @GetMapping("/hello")
    public ResponseEntity<String> home(){
        return ResponseEntity.ok("Hello World");
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO){
        return new ResponseEntity<>(userService.saveUser(userDTO),HttpStatus.CREATED);
    }


}
