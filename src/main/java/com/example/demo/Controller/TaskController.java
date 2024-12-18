package com.example.demo.Controller;

import com.example.demo.Dto.TaskDTO;
import com.example.demo.Services.TaskService;
import jakarta.validation.Valid;
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

    @GetMapping("/status/{statusId}")
    public ResponseEntity<?> getTasksByStatus(@PathVariable long statusId){
        return new ResponseEntity<>(taskService.getTasksByStatus(statusId),HttpStatus.OK);
    }

    @GetMapping("/userId")
    public ResponseEntity<?> getTasksByUser(@RequestParam long userId){
        return new ResponseEntity<>(taskService.getTaskByUser(userId),HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterTask(
            @RequestParam(required = false) Long userId ,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) Long idTask){
        return new ResponseEntity<>(taskService.filter(userId,statusId,idTask),HttpStatus.OK);
    }

}
