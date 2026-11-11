package com.student.student_management.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.student.student_management.dto.EnrollmentRequest;
import com.student.student_management.dto.EnrollmentResponse;

import com.student.student_management.entity.Student;
import com.student.student_management.entity.Course;
import com.student.student_management.entity.Enrollment;

import com.student.student_management.repository.StudentRepository;
import com.student.student_management.service.EnrollmentService;
import com.student.student_management.repository.CourseRepository;
import com.student.student_management.repository.EnrollmentRepository;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentServiceImpl(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {

        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAllEnrollments() {

        return enrollmentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponse getEnrollmentById(Long id) {

        Enrollment enrollment = enrollmentRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Enrollment not found with ID: " + id
                ));

        return convertToDTO(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByStudentId(
            Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Student not found with ID: " + studentId
            );
        }

        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public EnrollmentResponse createEnrollment(
            EnrollmentRequest request) {

        Long studentId = request.getStudentId();
        Long courseId = request.getCourseId();

        // Validate student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student not found with ID: " + studentId
                ));

        // Validate course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Course not found with ID: " + courseId
                ));

        // Prevent duplicate enrollment
        if (enrollmentRepository
                .existsByStudentIdAndCourseId(studentId, courseId)) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Student is already enrolled in this course"
            );
        }

        Enrollment enrollment = new Enrollment();

        enrollment.setStudent(student);
        enrollment.setCourse(course);

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        return convertToDTO(savedEnrollment);
    }

    @Override
    public EnrollmentResponse updateEnrollment(
            Long id,
            EnrollmentRequest request) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Enrollment not found with ID: " + id
                ));

        Long studentId = request.getStudentId();
        Long courseId = request.getCourseId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student not found with ID: " + studentId
                ));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Course not found with ID: " + courseId
                ));

        boolean duplicate = enrollmentRepository
                .existsByStudentIdAndCourseId(studentId, courseId);

        boolean sameEnrollment =
                enrollment.getStudent().getId().equals(studentId)
                && enrollment.getCourse().getId().equals(courseId);

        if (duplicate && !sameEnrollment) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Student is already enrolled in this course"
            );
        }

        enrollment.setStudent(student);
        enrollment.setCourse(course);

        Enrollment updatedEnrollment =
                enrollmentRepository.save(enrollment);

        return convertToDTO(updatedEnrollment);
    }

    @Override
    public void deleteEnrollment(Long id) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Enrollment not found with ID: " + id
                ));

        enrollmentRepository.delete(enrollment);
    }

    // Convert Entity to Response DTO
    private EnrollmentResponse convertToDTO(
            Enrollment enrollment) {

        return new EnrollmentResponse(
            enrollment.getId(),
            enrollment.getStudent().getId(),
            enrollment.getCourse().getId(),
            (enrollment.getStudent().getFirstName() + enrollment.getStudent().getLastName()),
            enrollment.getCourse().getCourseName(),
            enrollment.getCourse().getCourseCode()
        );
    }
}
