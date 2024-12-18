package com.example.demo.Repository;

import com.example.demo.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("SELECT t FROM Task t WHERE t.status.id = ?1")
    List<?> getTasksByStatus(Long id);

    @Query("SELECT t FROM Task t WHERE t.status.id = ?1 and t.user.id = ?2")
    List<?> findByStatusIdAndUserId(Long statusId, Long userId);

    @Query("SELECT t FROM Task t WHERE t.status.id = ?1 and t.user.id = ?2 and t.id = 3?")
    List<?> findByStatusIdAndUserIdAndTasId(Long statusId, Long userId, Long taskId);

}
