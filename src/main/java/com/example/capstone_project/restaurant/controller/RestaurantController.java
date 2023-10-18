package com.example.capstone_project.restaurant.controller;

import com.example.capstone_project.restaurant.dto.RestaurantDto;
import com.example.capstone_project.restaurant.service.RestaurantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;


    // 음식점 검색 endPoint
    @GetMapping("/search")
    public Page<RestaurantDto> search(
            @RequestParam("target") String target,
            @RequestParam(value = "page", defaultValue = "0") int pageNum,
            @RequestParam(value = "limit", defaultValue = "10") int pageSize
    )  {
        return restaurantService.searchRestaurant(target, pageNum, pageSize);
    }

}
