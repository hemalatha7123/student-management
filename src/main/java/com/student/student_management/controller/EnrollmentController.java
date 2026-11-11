
package com.student.student_management.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.student.student_management.dto.EnrollmentRequest;
import com.student.student_management.dto.EnrollmentResponse;
import com.student.student_management.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "http://localhost:4200")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // GET all enrollments or filter by student ID
    @GetMapping
    public ResponseEntity<List<EnrollmentResponse>> getAllEnrollments(
            @RequestParam(required = false) Long studentId) {

        List<EnrollmentResponse> enrollments;

        if (studentId != null) {
            enrollments = enrollmentService
                    .getEnrollmentsByStudentId(studentId);
        } else {
            enrollments = enrollmentService.getAllEnrollments();
        }

        return ResponseEntity.ok(enrollments);
    }

    // GET enrollment by ID
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> getEnrollmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            enrollmentService.getEnrollmentById(id)
        );
    }

    // CREATE enrollment
    @PostMapping("/create")
    public ResponseEntity<EnrollmentResponse> createEnrollment(
            @Valid @RequestBody EnrollmentRequest request) {

        EnrollmentResponse response =
                enrollmentService.createEnrollment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // UPDATE enrollment
    @PutMapping("/update/{id}")
    public ResponseEntity<EnrollmentResponse> updateEnrollment(
            @PathVariable Long id,
            @Valid @RequestBody EnrollmentRequest request) {

        EnrollmentResponse response =
                enrollmentService.updateEnrollment(id, request);

        return ResponseEntity.ok(response);
    }

    // DELETE enrollment
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEnrollment(
            @PathVariable Long id) {

        enrollmentService.deleteEnrollment(id);

        return ResponseEntity.noContent().build();
    }
}
