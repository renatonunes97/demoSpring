package com.example.demo.Controller;

import com.example.demo.Dto.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Security.Jwt.TokenResponse;
import com.example.demo.Security.service.AuthenticationService;
import com.example.demo.Services.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users Controllers", description = " Controller")
public class UserController {

    @Autowired
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;

    }

    @Operation(summary = "hello controller Users" , description = "retorna uma menssagem")
    @ApiResponse(responseCode = "200",description = "SUCESSO")
    @GetMapping("/hello")
    public ResponseEntity<String> home(Authentication authentication){

            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not Auth");
            }
            return ResponseEntity.ok("Welcome: "+authentication.getName());

    }

    @Operation(summary = "Get all Users" , description = "retorna todos os users criados")
    @ApiResponse(responseCode = "200",description = "SUCESSO")
    @GetMapping
    public List<?> getAllUsers(){
        return userService.getAll();
    }

    @Operation(summary = "Get Users" , description = "retorna todos os users menos o UserAuth")
    @ApiResponse(responseCode = "200",description = "SUCESSO")
    @GetMapping("/getUsers")
    public List<?> getUsers(Authentication authentication){
        return userService.getUsers(authentication);
    }


    @Operation(summary = "Insert User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully inserted ",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Error in the information sent to the server or lack of mandatory information"),
    })
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
    @Operation(summary = "Get Users info", description = "Returns a list Users Filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Error in the information sent to the server or the lack of mandatory information"),
    })
    @GetMapping("/filter")
    public ResponseEntity<?> filterUser(
            @RequestParam (required = false) Long userId,
            @RequestParam (required = false) String name,
            @RequestParam (required = false) String address,
            @RequestParam (required = false) String email){
        return new ResponseEntity<>(userService.filter(userId,name,address,email),HttpStatus.OK);
    }



}
