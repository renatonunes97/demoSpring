package com.example.demo.Controller;

import com.example.demo.Dto.UserDTO;
import com.example.demo.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ControllerUser {

    @Autowired
    private UserService userService;



    @GetMapping("/hello")
    public ResponseEntity<String> home(){
        return ResponseEntity.ok("Hello Users");
    }

    @GetMapping
    public List<?> getAllUsers(){
        return userService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO){
        return new ResponseEntity<>(userService.save(userDTO),HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestParam long userId){
        return new ResponseEntity<>(userService.delete(userId),HttpStatus.OK);
    }


}
