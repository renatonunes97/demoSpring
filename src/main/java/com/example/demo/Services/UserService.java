package com.example.demo.Services;


import com.example.demo.Dto.TaskDTO;
import com.example.demo.Dto.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class UserService implements GenericService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Override
    public Object convertToDTO(Object object) {
        if(object instanceof User user) {
            return new UserDTO(user.getId(),user.getName(),user.getAddress(),user.getEmail());
        }else {
            throw new IllegalArgumentException("Invalid object type. Expected User.");
        }
    }

    @Override
    public Object save(Object object) {
        if(object instanceof UserDTO userDTO){
            User user = (User) convertToEntity(userDTO);
            userRepository.save(user);
            return convertToDTO(user);
        }else{
            throw new IllegalArgumentException("Invalid object type. Expected User.");
        }
    }

    @Override
    public Object convertToEntity(Object object) {
        if(object instanceof UserDTO userDTO){
            return modelMapper.map(userDTO,User.class);
        }else{
            throw new IllegalArgumentException("Invalid object type. Expected UserDTO.");
        }
    }

    @Override
    public Object delete(Long userId) {
        return userRepository.findById(userId).map(user -> {
            if(user.getTasks().isEmpty()){
                userRepository.deleteById(user.getId());
                return convertToDTO(user);
            }else{
                throw new RuntimeException("user have tasks associated");
            }
        }).orElseThrow(()-> new RuntimeException("User Not found"));
    }

    public Object filter(String name, String address, String email){

    }



}
