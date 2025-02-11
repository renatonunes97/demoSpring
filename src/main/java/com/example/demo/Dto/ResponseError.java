package com.example.demo.Dto;

import java.util.List;

public record ResponseError(int status, String message, String error) {
}
