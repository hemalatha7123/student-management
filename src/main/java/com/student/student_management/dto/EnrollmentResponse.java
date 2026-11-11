package com.student.student_management.dto;

public class EnrollmentResponse {

    private Long id;
    private Long studentId;
    private Long courseId;
    private String studentName;
    private String courseName;
    private String courseCode;

    public EnrollmentResponse() {
    }

    public EnrollmentResponse(
            Long id,
            Long studentId,
            Long courseId,
            String studentName,
            String courseName,
            String courseCode) {

        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.studentName = studentName;
        this.courseName = courseName;
        this.courseCode = courseCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
