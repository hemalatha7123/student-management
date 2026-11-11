package com.student.student_management.dto;

import java.util.List;

public class CourseResponse {

	private Long id;
    private String courseName;
    private String courseCode;
    private String description;
    private Integer duration;
    private String status;
    //private List<StudentResponse> students;

    public CourseResponse(String courseName, String courseCode, 
    		String description, Integer duration, String status, Long id) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.description = description;
        this.duration = duration;
        this.status = status;
        this.id = id;
    }

    /**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
