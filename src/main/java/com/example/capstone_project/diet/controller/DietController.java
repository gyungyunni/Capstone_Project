package com.example.capstone_project.diet.controller;

import com.example.capstone_project.diet.dto.ReadDietDto;
import com.example.capstone_project.diet.service.DietService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("")
public class DietController {
    private final DietService dietService;

    //chat-gpt 와 간단한 채팅 서비스 소스
    @PostMapping("/api/v1/chat-gpt")
    public ReadDietDto test(
            @RequestParam(name = "gender", defaultValue = "") String gender,
            @RequestParam(name = "age", defaultValue = "0") Integer age,
            @RequestParam(name = "height", defaultValue = "0") Float height,
            @RequestParam(name = "weight", defaultValue = "0") Float weight,
            @RequestParam(name = "activity", defaultValue = "0") String activity,
            @RequestParam(name = "purpose", defaultValue = "0") String purpose
    ) {
        return dietService.getChatResponse(gender, age, height, weight, activity, purpose);
    }
}