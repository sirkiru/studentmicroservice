package com.myschool.studentmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Student data transfer object")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    @Schema(description = "Full name of the student", example = "John Doe")
    private String name;

    @Schema(description = "Email address", example = "john.doe@school.com")
    private String email;

    @Schema(description = "Age in years", example = "20")
    private int age;

}