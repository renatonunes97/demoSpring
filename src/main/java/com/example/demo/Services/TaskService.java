package com.example.demo.Services;

import com.example.demo.Dto.TaskDTO;
import com.example.demo.Dto.UserDTO;
import com.example.demo.Entity.StatusTask;
import com.example.demo.Entity.Task;
import com.example.demo.Entity.User;
import com.example.demo.Repository.StatusRepository;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class TaskService implements GenericService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;


    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ModelMapper modelMapper, StatusRepository statusRepository, ModelMapper modelMapper1) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
    }


    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }


    @Override
    public List<?> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public Object convertToDTO(Object object) {
        if(object instanceof Task task){
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(task.getId());
            taskDTO.setDescription(task.getDescription());
            taskDTO.setStatusID(task.getStatus() !=null ? task.getStatus().getId(): 0);
            taskDTO.setUserID(task.getUser() != null ? task.getUser().getId() : 0);
            return taskDTO;
        }else{
            throw new IllegalArgumentException("Invalid object type. Expected Task.");
        }
    }

    @Override
    public Object save(Object object) {
        if(object instanceof TaskDTO taskDTO){
            Task task = (Task) convertToEntity(taskDTO);
            taskRepository.save(task);
            return convertToDTO(task);
        }else{
            throw new IllegalArgumentException("Invalid object type. Expected TaskDTO.");
        }
    }

    @Override
    public Object convertToEntity(Object object) {
        if(object instanceof TaskDTO taskDTO){
            Task task = new Task();
            task.setId(taskDTO.getId());
            task.setDescription(taskDTO.getDescription());
            User user = userRepository.findById(taskDTO.getUserID()).orElseThrow(()->new RuntimeException("User not exist"));
            task.setUser(user);
            StatusTask statusTask = statusRepository.findById(taskDTO.getStatusID()).orElseThrow(()-> new RuntimeException("Status is not valid"));
            task.setStatus(statusTask);
            return task;
        }else{
            throw new IllegalArgumentException("Invalid object type. Expected TaskDTO.");
        }
    }

    @Override
    public Object delete(Long id) {
        return taskRepository.findById(id).map(task -> {taskRepository.deleteById(task.getId());
            return convertToDTO(task);
        }).orElseThrow(()-> new RuntimeException("Task Not found"));
    }

    public List<?> getTasksByStatus(long idStatus) {
        return taskRepository.getTasksByStatus(idStatus)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<?> getTaskByUser(long idUser){
        User users = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return users.getTasks().stream().map(this::convertToDTO).toList();
    }

    public List<?> filter(Long idUser, Long statusId, Long idTask){
        if(idTask != null && idUser != null && statusId != null){
            return taskRepository.findByStatusIdAndUserIdAndTasId(statusId,idUser,idTask)
                    .stream()
                    .map(this::convertToDTO).toList();
        } else if( idUser != null && statusId != null ){
            return taskRepository.findByStatusIdAndUserId(statusId,idUser)
                    .stream()
                    .map(this::convertToDTO).toList();
        }else if(statusId != null){
            return taskRepository.getTasksByStatus(statusId)
                    .stream()
                    .map(this::convertToDTO).toList();
        }else if (idUser !=null) {
            return getTaskByUser(idUser);
        }else if(idTask != null) {
            return taskRepository.findById(idTask)
                    .stream()
                    .map(this::convertToDTO).toList();
        }else{
            return getAllTask();
        }
    }




}
