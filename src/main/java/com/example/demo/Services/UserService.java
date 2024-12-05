package com.example.demo.Services;


import com.example.demo.Dto.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public UserDTO convertToDTO(User user){
        return new UserDTO(user.getId(),user.getName(),user.getAddress(),user.getEmail());
    }

    public UserDTO saveUser(UserDTO userDTO){
        User user = convertToEntity(userDTO);
        userRepository.save(user);
        return convertToDTO(user);
    }

    public User convertToEntity(UserDTO userDTO) {
        /*User user = new User();
        user.setName(userDTO.getName());
        user.setAddress(userDTO.getAddress());
         */
        return modelMapper.map(userDTO,User.class);
    }
}
