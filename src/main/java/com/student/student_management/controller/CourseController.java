package com.student.student_management.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.student_management.dto.CourseRequest;
import com.student.student_management.dto.CourseResponse;
import com.student.student_management.entity.Course;
import com.student.student_management.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {
	
	 private final CourseService courseService;

	    // Constructor injection
	    public CourseController(CourseService courseService) {
	        this.courseService = courseService;
	    }

	    @PostMapping("/create")
	    public ResponseEntity<Course> createCourse(
	            @Valid @RequestBody CourseRequest courseRequest) {

	        return ResponseEntity.status(HttpStatus.CREATED)
	            .body(courseService.createCourse(courseRequest));
	    }
	    
	    @GetMapping("/getAllCourses")
	    public ResponseEntity<List<CourseResponse>> getAllCourses() {

	    	/*
	        List<CourseResponse> response = courseService.getAllCourses()
	            .stream()
	            .map(course -> {
	                return new CourseResponse(
	                    course.getCourseName(),
	                    course.getCourseCode(),
	                    course.getDescription(),
	                    course.getDuration(),
	                    course.getStatus()
	                );
	            })
	            .collect(Collectors.toList());
	            */
	    	List<Course> courseList = courseService.getAllCourses();
	    	List<CourseResponse> responseList = new ArrayList();

	    	courseList.forEach(eachCourse -> {
	    		CourseResponse eachCourseDto = new CourseResponse(eachCourse.getCourseName(), 
	    				eachCourse.getCourseCode(), eachCourse.getDescription(), 
	    				eachCourse.getDuration(), eachCourse.getStatus(), eachCourse.getId());
	    		responseList.add(eachCourseDto);
	    	});
	        return ResponseEntity.ok(responseList);
	    }

	    
	    @GetMapping("/{id}")
	    public ResponseEntity<CourseResponse> getCourse(
	            @PathVariable Long id) {
	    	
	    	Course course = courseService.getCourseById(id);
	    	
	    	CourseResponse response = new CourseResponse(
	    			course.getCourseName(),
	    			course.getCourseCode(),
	    			course.getDescription(),
	    			course.getDuration(),
	    			course.getStatus(),
	    			course.getId()
	            );

	        return ResponseEntity.ok(response);
	    }

	    @PutMapping("/update/{id}")
	    public ResponseEntity<Course> updateCourse(
	            @PathVariable Long id,
	            @Valid @RequestBody CourseRequest request) {

	        return ResponseEntity.ok(
	            courseService.updateCourse(id, request));
	    }

	    @DeleteMapping("/delete/{id}")
	    public ResponseEntity<Void> deleteCourse(
	            @PathVariable Long id) {

	        courseService.deleteCourse(id);

	        return ResponseEntity.noContent().build();
	    }

}
