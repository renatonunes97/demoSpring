package com.example.demo.Services;

import com.example.demo.Dto.TaskDTO;
import com.example.demo.Entity.StatusTask;
import com.example.demo.Entity.Task;
import com.example.demo.Entity.User;
import com.example.demo.Repository.StatusRepository;
import com.example.demo.Repository.TaskRepository;
import com.example.demo.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
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
            taskDTO.setUserID(task.getUser() != null ? task.getUser().getId() : null);
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
            task.setDescription(taskDTO.getDescription());

            if (taskDTO.getUserID() != null) {
                User user = userRepository.findById(taskDTO.getUserID())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                task.setUser(user);
            } else {
                task.setUser(null);  // Ou você pode deixar o usuário como null, conforme o comportamento desejado
            }

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

    public List<?> filter(Long idUser, Long statusId, Long idTask) {
            return getAllTask().stream()
                    .filter(task ->  statusId == null || task.getStatus().getId().equals(statusId))
                    .filter(task ->  idUser  == null || task.getUser().getId().equals(idUser))
                    .filter(task ->  idTask  == null || task.getId().equals(idTask))
                    .map(this::convertToDTO).toList();
    }

    public Object addTaskByUser(Long idUser, Long idTask) {
        User user = userRepository.findById(idUser).orElseThrow(() -> new RuntimeException("User don´t exist "));
        Task task = taskRepository.findById(idTask).orElseThrow(() -> new RuntimeException("Task Not Found "));
        if (task.getUser() == null) {
            user.getTasks().add(task);
            userRepository.save(user);
            task.setUser(user);
            taskRepository.save(task);
            return "Task successfully added to user"+ user.getName();
        }
        else {
            throw new RuntimeException("Task associated another user: id: " + task.getUser().getId() + ", Name:" + task.getUser().getName());
        }
    }

}
