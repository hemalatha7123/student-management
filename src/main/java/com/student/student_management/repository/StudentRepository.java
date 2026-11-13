package com.student.student_management.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.student_management.entity.Student;

public interface StudentRepository
        extends JpaRepository<Student, Long> {

	// Check if the student exists by email
    boolean existsByEmail(String email);
    
    // Find student details by Id
    public Optional<Student> findById(Long id);
	
    // Get all students
	public List<Student> findAll();
	
	// Delete student by Id
	public void deleteById(Long id);
	
	// Create a student record
	public Student save(Student student);
	
	// Delete the record
	public void delete(Student student);

}