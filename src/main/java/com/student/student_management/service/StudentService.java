package com.student.student_management.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.student.student_management.dto.*;
import com.student.student_management.entity.*;
import com.student.student_management.repository.*;
import com.student.student_management.utils.StudentManagementUtils;
import com.student.student_management.exception.*;

@Service
@Transactional
public class StudentService {

    private final StudentRepository repository;;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student createStudent(StudentRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                "Student email already exists");
        }

        Student student = new Student();

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setDateOfBirth(request.getDateOfBirth());

        return repository.save(student);
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {

        return repository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Student not found: " + id));
    }

    // Update the student record
    public Student updateStudent(
            Long id, StudentRequest request) {

        Student student = getStudentById(id);

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());

        return repository.save(student);
    }

    // Donot delete the complete record from DB. Instead make the status as INACTIVE for Audit purpose
    public void deleteStudent(Long id) {

        Student student = getStudentById(id);

        if (!student.getCourses().isEmpty()) {
            throw new IllegalStateException(
                "Remove student enrollments before deletion");
        }
        
        student.setStatus(StudentManagementUtils.STATUS_INACTIVE);

        repository.save(student);
    }
}