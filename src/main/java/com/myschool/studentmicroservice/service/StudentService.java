package com.myschool.studentmicroservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.myschool.studentmicroservice.dto.CourseDetailsDTO;
import com.myschool.studentmicroservice.dto.StudentDTO;
import com.myschool.studentmicroservice.dto.StudentWithCoursesDTO;
import com.myschool.studentmicroservice.exception.StudentNotFoundException;
import com.myschool.studentmicroservice.model.Student;
import com.myschool.studentmicroservice.repo.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private RestTemplate restTemplate;

    public Student createStudent(StudentDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setAge(dto.getAge());
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));
    }

    public Student updateStudent(String id, StudentDTO dto) {
        Student student = getStudentById(id);
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setAge(dto.getAge());
        return studentRepository.save(student);
    }

    public void deleteStudent(String id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    public Student enrollInCourse(String studentId, String courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));

        // Initialize list if null
        if (student.getEnrolledCourseIds() == null) {
            student.setEnrolledCourseIds(new ArrayList<>());
        }

        // Add course if not already enrolled
        if (!student.getEnrolledCourseIds().contains(courseId)) {
            student.getEnrolledCourseIds().add(courseId);
            return studentRepository.save(student);
        }

        return student; // Return student without changes if already enrolled
    }

    public StudentWithCoursesDTO getStudentWithCourses(String studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));

        List<CourseDetailsDTO> courses = List.of();
        if (student.getEnrolledCourseIds() != null && !student.getEnrolledCourseIds().isEmpty()) {
            // Build URI with proper encoding
            String url = "http://course-service/api/courses/bulk?ids=" +
                    String.join(",", student.getEnrolledCourseIds());

            // Make the request
            ResponseEntity<List<CourseDetailsDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CourseDetailsDTO>>() {
                    });

            courses = response.getBody() != null ? response.getBody() : List.of();
        }

        return new StudentWithCoursesDTO(student, courses);
    }

    public List<Student> getStudentsByIds(List<String> ids) {
        return studentRepository.findAllById(ids);
    }
}
