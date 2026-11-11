
package com.student.student_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.student_management.entity.Enrollment;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {

    // Find all courses enrolled by a student
    List<Enrollment> findByStudentId(Long studentId);

    // Prevent duplicate student-course enrollment
    boolean existsByStudentIdAndCourseId(
        Long studentId,
        Long courseId
    );

}
