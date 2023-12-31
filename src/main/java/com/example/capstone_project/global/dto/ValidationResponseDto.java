package com.example.capstone_project.global.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class ValidationResponseDto {
    private List<Content> contentList;
    private HttpStatus statusCode;

    @Data
    public static class Content {
        private String fieldName;
        private String message;
    }
}
