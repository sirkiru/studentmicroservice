package com.myschool.studentmicroservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myschool.studentmicroservice.dto.ErrorResponseDTO;
import com.myschool.studentmicroservice.dto.StudentDTO;
import com.myschool.studentmicroservice.dto.StudentDetailsDTO;
import com.myschool.studentmicroservice.dto.StudentWithCoursesDTO;
import com.myschool.studentmicroservice.exception.StudentNotFoundException;
import com.myschool.studentmicroservice.model.Student;
import com.myschool.studentmicroservice.service.StudentService;

import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/students")
@Timed("student.api.calls") // Track time for all methods
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody StudentDTO dto) {
        return ResponseEntity.ok(studentService.createStudent(dto));
    }

    @GetMapping
    @Timed(value = "student.get.all") // Specific metric
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @ApiResponse(responseCode = "404", description = "Student not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(
            @Parameter(description = "ID of the student to retrieve", required = true, example = "65a1b2c3d4e5f6g7h8i9j0k") @PathVariable String id) {
        try {
            return ResponseEntity.ok(studentService.getStudentById(id));
        } catch (StudentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @RequestBody StudentDTO dto) {
        try {
            return ResponseEntity.ok(studentService.updateStudent(id, dto));
        } catch (StudentNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (StudentNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{studentId}/enroll/{courseId}")
    public ResponseEntity<Student> enrollInCourse(
            @PathVariable String studentId,
            @PathVariable String courseId) {
        Student student = studentService.enrollInCourse(studentId, courseId);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<StudentWithCoursesDTO> getStudentWithCourses(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getStudentWithCourses(id));
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<StudentDetailsDTO>> getStudentsByIds(@RequestParam List<String> ids) {
        List<Student> students = studentService.getStudentsByIds(ids);
        List<StudentDetailsDTO> studentDTOs = students.stream()
                .map(s -> new StudentDetailsDTO(
                        s.getId(),
                        s.getName(),
                        s.getEmail(),
                        s.getAge()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentDTOs);
    }
}
