package com.example.capstone_project.global.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JwtTokenInfoDto {
    private String accessToken;
    private String refreshToken;
}
