
package com.student.student_management.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EnrollmentRequest {

    @NotNull(message = "Student ID is required")
    @Positive(message = "Student ID must be positive")
    private Long studentId;

    @NotNull(message = "Course ID is required")
    @Positive(message = "Course ID must be positive")
    private Long courseId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
