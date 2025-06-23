package com.myschool.studentmicroservice.dto;

import lombok.Data;

@Data
public class CourseDetailsDTO {
    private String id;
    private String code;
    private String title;
    private String description;
    private int credits;
}