package com.example.capstone_project.restaurant.service;

import com.example.capstone_project.restaurant.dto.PlaceDataDto;
import com.example.capstone_project.restaurant.dto.RestaurantDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RestaurantService {

    // target 을 query 로
    // naver maps api 호출
    // 결과를 리스트에 저장해 반환
    public List<PlaceDataDto> getPlaceList(String target, Integer displayCount) {
        log.info("Search KeyWord : {}", target);
        List<PlaceDataDto> placeDataDtoList = new ArrayList<>();
        String url = "https://map.naver.com/v5/api";
        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Whale/3.21.192.22 Safari/537.36")
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(10 * 1024 * 1024)
                )
                .build();
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("caller", "pcweb")
                        .queryParam("query", target)
                        .queryParam("type", "all")
                        .queryParam("page", 1)
                        .queryParam("displayCount", displayCount)
                        .queryParam("lang", "ko")
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .retry(3)
                .block();
        try {
            JsonNode originJson = new ObjectMapper().readTree(response);
            JsonNode placeList = originJson.get("result").get("place").get("list");

            for (JsonNode place : placeList) {
                PlaceDataDto placeDataDto = new ObjectMapper().treeToValue(place, PlaceDataDto.class);
                String businessHours = place.get("businessStatus").get("businessHours").textValue();
                String openHours = businessHours != null && !businessHours.isBlank() ? businessHours.split("~")[0].substring(8) : null;
                String closeHours = businessHours != null && !businessHours.isBlank() ? businessHours.split("~")[1].substring(8) : null;
                placeDataDto.setOpenHours(openHours);
                placeDataDto.setCloseHours(closeHours);
                placeDataDtoList.add(placeDataDto);
            }
            return placeDataDtoList;
        } catch (Exception ex) {
            log.warn("Error message : {} | {} : failed", ex.getMessage(), target);
        }
        return null;
    }

    // 사용자의 검색어를 기준으로
    // naver maps api 호출 후
    // 결과를 page 로 반환
    public Page<RestaurantDto> searchRestaurant(String target, Integer pageNumber, Integer pageSize) {
        List<PlaceDataDto> placeDataDtoList = getPlaceList(target + " " + "음식점", 100);
        if (placeDataDtoList == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 검색어의 결과가 없음");

        List<RestaurantDto> restaurantDtoList = new ArrayList<>();
        for (PlaceDataDto place : placeDataDtoList) {
            restaurantDtoList.add(RestaurantDto.fromDto(place));
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), restaurantDtoList.size());
        try {
            return new PageImpl<>(restaurantDtoList.subList(start, end), pageable, restaurantDtoList.size());
        } catch (IllegalArgumentException ex) {
            return new PageImpl<>(restaurantDtoList.subList(restaurantDtoList.size(), restaurantDtoList.size()), pageable, restaurantDtoList.size());
        }
    }

}
