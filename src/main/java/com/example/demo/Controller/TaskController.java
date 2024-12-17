package com.example.demo.Controller;

import com.example.demo.Dto.TaskDTO;
import com.example.demo.Dto.UserDTO;
import com.example.demo.Services.TaskService;
import jakarta.validation.Valid;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/task")
public class TaskController {


    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> home(){
        return ResponseEntity.ok("Hello Task´s");
    }

    @GetMapping
    public List<?> getAllTasks(){
        return taskService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskDTO taskDTO){
        return new ResponseEntity<>(taskService.save(taskDTO), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTask(@RequestParam long taskId){
        return new ResponseEntity<>(taskService.delete(taskId),HttpStatus.OK);
    }
}