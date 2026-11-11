package com.student.student_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.student.student_management.entity.*;

public interface CourseRepository
        extends JpaRepository<Course, Long> {

    boolean existsByCourseCode(String courseCode);
    
    // Find course details by Id
    public Optional<Course> findById(Long id);
	
    // Get all courses
	public List<Course> findAll();
	
	// Delete course by Id
	public void deleteById(Long id);
	
	// Create a course record
	public Course save(Course course);
	
	// Delete the course
	public void delete(Course course);
}