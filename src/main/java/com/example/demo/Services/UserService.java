package com.example.demo.Services;
import com.example.demo.Dto.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

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

    public Object getUser(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public Object convertToDTO(Object object) {
        if(object instanceof User user) {
            return new UserDTO(user.getId(),user.getName(),user.getAddress(), user.getPassword(),user.getEmail());
        }else {
            throw new IllegalArgumentException("Invalid object type. Expected User.");
        }
    }



    @Override
    public Object save(Object object) {
        if(object instanceof UserDTO userDTO){
            if(!userRepository.existsByUsername(userDTO.getName())) {
                User user = (User) convertToEntity(userDTO);
                userRepository.save(user);
                return convertToDTO(user);
            }else
                throw new RuntimeException("User ja existe");
        }else{
            throw new IllegalArgumentException("Invalid object type. Expected User.");
        }
    }

    @Override
    public Object convertToEntity(Object object) {
        if(object instanceof UserDTO userDTO){
            //return modelMapper.map(userDTO,User.class);
            User user = new User();
            user.setName(userDTO.getName());
            user.setAddress(userDTO.getAddress());
            user.setPassword( new BCryptPasswordEncoder().encode(userDTO.getPassword()));
            user.setEmail(userDTO.getEmail());
            user.setRoles("ROLE_USER");
            user.setTasks(null);
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
