package com.example.capstone_project.restaurant.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RestaurantDto {
    private String name;
    private String address;
    private String roadAddress;
    private BigDecimal mapX;
    private BigDecimal mapY;

    public static RestaurantDto fromDto(PlaceDataDto placeDataDto) {
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName(placeDataDto.getName());
        restaurantDto.setAddress(placeDataDto.getAddress());
        restaurantDto.setRoadAddress(placeDataDto.getRoadAddress());
        restaurantDto.setMapX(placeDataDto.getX());
        restaurantDto.setMapY(placeDataDto.getY());
        return restaurantDto;
    }
}
