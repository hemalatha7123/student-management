package com.student.student_management.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.student.student_management.dto.CourseResponse;
import com.student.student_management.dto.StudentRequest;
import com.student.student_management.dto.StudentResponse;
import com.student.student_management.entity.Student;
import com.student.student_management.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Student APIs",
	description = "Operations related to students")
public class StudentController {
	
	private static final Logger logger =
            LoggerFactory.getLogger(StudentController.class);

    private final StudentService service;

    // Constructor injection
    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Student> createStudent(
            @Valid @RequestBody StudentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.createStudent(request));
    }

    /*
    @GetMapping("/getAllStudents")
    public ResponseEntity<List<Student>> getAllStudents_old() {
        return ResponseEntity.ok(service.getAllStudents());
    }*/
    
    
    @GetMapping("/getAllStudents")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {

        List<StudentResponse> response = service.getAllStudents()
            .stream()
            .map(student -> {

                List<CourseResponse> courses =
                    student.getCourses().stream()
                        .map(course -> new CourseResponse(
                            course.getCourseName(),
                            course.getCourseCode(),
                            course.getDescription(),
                            course.getDuration(),
                            course.getStatus(),
                            course.getId()
                        ))
                        .collect(Collectors.toList());

                return new StudentResponse(
                    student.getId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getDateOfBirth(),
                    student.getStatus(),
                    courses
                );
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get student by ID")
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudent(
            @PathVariable Long id) {
    	
    	logger.info("Inside getStudent details by Id : Id : "+id);
    	Student student = service.getStudentById(id);
    	List<CourseResponse> courses =
                student.getCourses().stream()
                    .map(course -> new CourseResponse(
                        course.getCourseName(),
                        course.getCourseCode(),
                        course.getDescription(),
                        course.getDuration(),
                        course.getStatus(),
                        course.getId()
                    ))
                    .collect(Collectors.toList());
    	
    	StudentResponse response = new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getPhone(),
                student.getDateOfBirth(),
                student.getStatus(),
                courses
            );

    	logger.info("Response of getStudent details by Id : response : "+response);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update student details by ID")
    @PutMapping("/update/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {

        return ResponseEntity.ok(
            service.updateStudent(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id) {

        service.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }
}