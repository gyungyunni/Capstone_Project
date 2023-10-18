package com.example.capstone_project.diet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReadDietDto {
    private String morning;
    private String lunch;
    private String dinner;

    public ReadDietDto(){

    }
}
