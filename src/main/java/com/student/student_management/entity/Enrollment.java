package com.student.student_management.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "enrollments",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"student_id", "course_id"}
    )
)
public class Enrollment {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "student_id", nullable = false)
	    private Student student;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "course_id", nullable = false)
	    private Course course;

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

		/**
		 * @return the student
		 */
		public Student getStudent() {
			return student;
		}

		/**
		 * @param student the student to set
		 */
		public void setStudent(Student student) {
			this.student = student;
		}

		/**
		 * @return the course
		 */
		public Course getCourse() {
			return course;
		}

		/**
		 * @param course the course to set
		 */
		public void setCourse(Course course) {
			this.course = course;
		}
}
