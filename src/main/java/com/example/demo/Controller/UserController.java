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
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


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
        try {
            String response = (String) userService.delete(userId);
            return ResponseEntity.ok(response); // Sucesso
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterUser(
            @RequestParam (required = false) Long userId,
            @RequestParam (required = false) String name,
            @RequestParam (required = false) String address,
            @RequestParam (required = false) String email){
        return new ResponseEntity<>(userService.filter(userId,name,address,email),HttpStatus.OK);
    }



}
