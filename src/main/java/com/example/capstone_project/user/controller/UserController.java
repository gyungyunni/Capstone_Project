package com.example.capstone_project.user.controller;

import com.example.capstone_project.global.dto.ResponseDto;
import com.example.capstone_project.global.exception.CustomException;
import com.example.capstone_project.global.exception.ErrorCode;
import com.example.capstone_project.global.jwt.JwtTokenInfoDto;
import com.example.capstone_project.global.jwt.JwtTokenUtils;
import com.example.capstone_project.user.dto.*;
import com.example.capstone_project.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping("/login")
    // login 시도 시 redis 에서 해당 유저의 refresh token 이 있는지 확인
    // 1. login logic 에서 기존 refresh token 을 지우고 새로 토큰 발급
    // 2. 기존 refresh token 으로 재발급
    public JwtTokenInfoDto login(@RequestBody @Valid LoginDto request, @RequestParam(value = "autoLogin") String autoLogin, HttpServletResponse response) {
        JwtTokenInfoDto jwtTokenInfoDto = userService.loginUser(request);
        userService.setRefreshCookie(jwtTokenInfoDto.getRefreshToken(), autoLogin, response);
        return jwtTokenInfoDto;
    }

    @PostMapping("/reissue")
    public JwtTokenInfoDto reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        String autoLogin = null;
        try {
            for (Cookie cookie : request.getCookies()) {
                switch (cookie.getName()) {
                    case "REFRESH_TOKEN" -> refreshToken = cookie.getValue();
                    case "AUTO_LOGIN" -> autoLogin = cookie.getValue();
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        if (refreshToken == null || autoLogin == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "재로그인 필요");
        JwtTokenInfoDto jwtTokenInfoDto = jwtTokenUtils.regenerateToken(refreshToken);
        userService.setRefreshCookie(jwtTokenInfoDto.getRefreshToken(), autoLogin, response);
        userService.setAutoLoginCookie(autoLogin, response);
        return jwtTokenInfoDto;
    }

    @PostMapping("/join")
    public ResponseDto join(@RequestBody @Valid JoinDto request) {
        if (!request.getPasswordCheck().equals(request.getPassword()))
            throw new CustomException(ErrorCode.DIFF_PASSWORD_CHECK, String.format("Username : %s", request.getUsername()));
        userService.createUser(CustomUserDetails.fromDto(request));

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("회원가입이 완료되었습니다.");
        responseDto.setStatus(HttpStatus.OK);
        return responseDto;
    }

    @PostMapping("/logout")
    public ResponseDto logout(@AuthenticationPrincipal String username, HttpServletRequest request, HttpServletResponse response) {
        log.info(username);
        return userService.logout(username, request, response);
    }

    //회원정보조회
    @GetMapping("/profile")
    public UserProfile getProfile(@AuthenticationPrincipal String username) {
        return userService.readUser(username);
    }

    //회원정보수정
    @PutMapping("/profile")
    public ResponseDto update(@RequestBody UpdateProfileDto updateDto) {
        userService.updateUser(CustomUserDetails.fromDto(updateDto));
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("사용자 정보 수정이 완료되었습니다.");
        responseDto.setStatus(HttpStatus.OK);
        return responseDto;
    }

    @PutMapping("/profile/image")
    public ResponseDto uploadImage(MultipartFile multipartFile, @AuthenticationPrincipal String username) throws IOException {
        return userService.uploadProfileImage(username, multipartFile);
    }

    //회원탈퇴
    @DeleteMapping("/profile")
    public ResponseDto delete(@AuthenticationPrincipal String username) {
        userService.deleteUser(username);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("회원탈퇴가 완료되었습니다.");
        responseDto.setStatus(HttpStatus.OK);
        return responseDto;
    }

    @GetMapping("/username")
    public String getUsername(@AuthenticationPrincipal String username) {
        return username;
    }
}
