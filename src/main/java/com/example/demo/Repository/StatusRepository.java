package com.example.demo.Repository;

import com.example.demo.Entity.StatusTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<StatusTask,Long> {
}
