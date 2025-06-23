package com.myschool.studentmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StudentDetailsDTO {
    private String id;
    private String name;
    private String email;
    private int age;
}
