package com.example.demo.Repository;

import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    //@Query("SELECT COUNT(u) > 0 FROM Users u WHERE u.name = ?1")
    boolean existsByName(String name);

    //@Query("SELECT u FROM Users u WHERE u.name = ?1")
    Optional<User> findByName(String username);
}
