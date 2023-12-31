package com.example.capstone_project.user.dto;

import com.example.capstone_project.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Builder
public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String gender;
    private Integer age;
    private String imgUrl;

    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
    public static CustomUserDetails fromEntity(User user) {
        return CustomUserDetails.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .phone(user.getPhone())
                .gender(user.getGender())
                .age(user.getAge())
                .imgUrl(user.getImgUrl())
                .build();
    }

    public static CustomUserDetails fromDto(JoinDto joinUser) {
        return CustomUserDetails.builder()
                .username(joinUser.getUsername())
                .password(joinUser.getPassword())
                .email(joinUser.getEmail())
                .phone(joinUser.getPhone())
                .gender(joinUser.getGender())
                .age(joinUser.getAge())
                .imgUrl("https://matprint.s3.ap-northeast-2.amazonaws.com/media/base.png")
                .build();
    }

    public static CustomUserDetails fromDto(UpdateProfileDto updateDto) {
        return CustomUserDetails.builder()
                .email(updateDto.getEmail())
                .phone(updateDto.getPhone())
                .gender(updateDto.getGender())
                .age(updateDto.getAge())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
