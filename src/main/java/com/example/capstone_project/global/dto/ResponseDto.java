package com.example.capstone_project.global.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class ResponseDto {
    private String message;
    private HttpStatus status;
}
