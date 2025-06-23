package com.myschool.studentmicroservice.dto;

import com.myschool.studentmicroservice.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentWithCoursesDTO {
    private String id;
    private String name;
    private String email;
    private int age;
    private List<CourseDetailsDTO> enrolledCourses;

    public StudentWithCoursesDTO(Student student, List<CourseDetailsDTO> courses) {
        this.id = student.getId();
        this.name = student.getName();
        this.email = student.getEmail();
        this.age = student.getAge();
        this.enrolledCourses = courses;
    }
}