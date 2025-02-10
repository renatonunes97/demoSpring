package com.example.demo.Services;
import com.example.demo.Dto.UserDTO;
import com.example.demo.Entity.Roles;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService implements GenericService {


    private final UserRepository userRepository;




    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public List<User> getAll(){
        return userRepository.findAll();
    }


    public List<User> getUsers(Authentication authentication){
        User userAuth = (User) getUser(authentication.getName());
        return userRepository.findAll()
                .stream()
                .filter(user -> !Objects.equals(user.getId(), userAuth.getId()))
                .collect(Collectors.toList());
    }

    public Object getUser(String username){
        return userRepository.findByName(username).orElse(null);
    }

    @Override
    public Object convertToDTO(Object object) {
        if(object instanceof User user) {
            UserDTO userDTO = new UserDTO();
            //userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setPassword(user.getPassword());
            userDTO.setAddress(user.getAddress());
            userDTO.setEmail(user.getEmail());
            return userDTO;
        }else {
            throw new IllegalArgumentException("Invalid object type. Expected User.");
        }
    }



    @Override
    @Transactional
    public Object save(Object object) {
        if(object instanceof UserDTO userDTO){
            if(!userRepository.existsByName(userDTO.getName())) {
                User user = (User) convertToEntity(userDTO);
                userRepository.save(user);
                return user;
            }else
                throw new RuntimeException("User ja existe");
        }else{
            throw new IllegalArgumentException("Invalid object type. Expected User.");
        }
    }

    @Override
    public Object convertToEntity(Object object) {
        if(object instanceof UserDTO userDTO){
            ModelMapper modelMapper = new ModelMapper();
            User user =modelMapper.map(userDTO,User.class);
            user.setRoles(Roles.ROLE_USER.name());
            return user;

        }else{
            throw new IllegalArgumentException("Invalid object type. Expected UserDTO.");
        }
    }

    @Override
    public String delete(Long userId) {
        return userRepository.findById(userId).map(user -> {
            if (user.getTasks().isEmpty()) {
                userRepository.deleteById(user.getId());
                return "Usuário deletado com sucesso"; // Retorna uma mensagem de sucesso
            } else {
                throw new RuntimeException("O usuário tem tarefas associadas e não pode ser deletado.");
            }
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public List<?> filter(Long idUser,String name, String address, String email){
        return getAll().stream()
                .filter(user -> name == null || user.getName().contains(name))
                .filter(user -> address == null || user.getAddress().contains(address))
                .filter(user -> idUser == null || user.getId().equals(idUser))
                .map(this::convertToDTO).toList();
    }



}
