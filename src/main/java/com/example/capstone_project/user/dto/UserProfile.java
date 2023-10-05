package com.example.capstone_project.user.dto;

import com.example.capstone_project.user.entity.User;
import lombok.Data;

@Data
public class UserProfile {
    private String username;
    private String phone;
    private String email;
    private String gender;
    private Integer age;
    private String imgUrl;

    public static UserProfile fromEntity(User user) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(user.getUsername());
        userProfile.setPhone(user.getPhone());
        userProfile.setEmail(user.getEmail());
        userProfile.setGender(user.getGender());
        userProfile.setAge(user.getAge());
        userProfile.setImgUrl(user.getImgUrl());
        return userProfile;
    }
}
