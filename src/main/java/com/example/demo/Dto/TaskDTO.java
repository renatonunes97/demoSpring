package com.example.demo.Dto;

import com.example.demo.Entity.StatusTask;
import com.example.demo.Entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class TaskDTO {


    private long id;

    @NotEmpty(message = "Description not empty")
    private String description;
    @NotNull(message = "status not be null")
    private long statusID;

    private long userID;


    public TaskDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStatusID() {
        return statusID;
    }

    public void setStatusID(long statusID) {
        this.statusID = statusID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
