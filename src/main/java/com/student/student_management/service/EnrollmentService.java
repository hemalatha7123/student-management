
package com.student.student_management.service;

import java.util.List;

import com.student.student_management.dto.EnrollmentRequest;
import com.student.student_management.dto.EnrollmentResponse;

public interface EnrollmentService {

    // Retrieve all enrollments
    List<EnrollmentResponse> getAllEnrollments();

    // Retrieve enrollment by ID
    EnrollmentResponse getEnrollmentById(Long id);

    // Retrieve enrollments by student ID
    List<EnrollmentResponse> getEnrollmentsByStudentId(
        Long studentId
    );

    // Create enrollment
    EnrollmentResponse createEnrollment(
        EnrollmentRequest request
    );

    // Update enrollment
    EnrollmentResponse updateEnrollment(
        Long id,
        EnrollmentRequest request
    );

    // Delete enrollment
    void deleteEnrollment(Long id);
}
