package com.example.capstone_project.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UpdateProfileDto {
    private String email;
    private String phone;
    private String gender;
    private Integer age;
}
