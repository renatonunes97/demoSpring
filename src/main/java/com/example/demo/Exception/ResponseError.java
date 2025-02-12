package com.example.demo.Exception;

import java.util.List;

public record ResponseError(int status, String message, List<ErrorParam> error) {
}
