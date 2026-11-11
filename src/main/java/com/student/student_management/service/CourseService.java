package com.student.student_management.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.student.student_management.dto.CourseRequest;
import com.student.student_management.entity.Course;
import com.student.student_management.exception.ResourceNotFoundException;
import com.student.student_management.repository.CourseRepository;
import com.student.student_management.utils.StudentManagementUtils;

@Service
public class CourseService {
	private final CourseRepository courseRepository;
	
	// Constructor Injection
    public CourseService(
            CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
	
	//createCourse(CourseRequest request);

	//getAllCourses();

	//getCourseById(Long id);

	//updateCourse(Long id, CourseRequest request);

	//deleteCourse(Long id);
	
	 public Course createCourse(CourseRequest request) {

	        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
	            throw new IllegalArgumentException(
	                "Course code already exists");
	        }

	        Course course = new Course();

	        course.setCourseCode(request.getCourseCode());
	        course.setCourseName(request.getCourseName());
	        course.setDescription(request.getDescription());
	        course.setDuration(request.getDuration());
	        
	        return courseRepository.save(course);
	    }
	 
	 @Transactional(readOnly = true)
	    public List<Course> getAllCourses() {
	        return courseRepository.findAll();
	    }

	    @Transactional(readOnly = true)
	    public Course getCourseById(Long id) {

	        return courseRepository.findById(id)
	            .orElseThrow(() ->
	                new ResourceNotFoundException(
	                    "Course not found: " + id));
	    }

	    // Update the student record
	    public Course updateCourse(
	            Long id, CourseRequest request) {

	    	Course course = getCourseById(id);

	    	course.setCourseCode(request.getCourseCode());
	    	course.setDescription(request.getDescription());
	    	course.setDuration(request.getDuration());
	    	course.setCourseName(request.getCourseName());
	        return courseRepository.save(course);
	    }

	    // Donot delete the complete record from DB. Instead make the status as INACTIVE for Audit purpose
	    public void deleteCourse(Long id) {

	        Course course = getCourseById(id);

	        /*
	        if (!course.getStatus().equals(StudentManagementUtils.STATUS_ACTIVE)) {
	            throw new IllegalStateException(
	                "Course is not available to delete");
	        }*/
	        
	        course.setStatus(StudentManagementUtils.STATUS_INACTIVE);

	        courseRepository.save(course);
	    }
}
