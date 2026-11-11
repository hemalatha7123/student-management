
package com.student.student_management.service;

import com.student.student_management.dto.CourseRequest;
import com.student.student_management.entity.Course;
import com.student.student_management.exception.ResourceNotFoundException;
import com.student.student_management.repository.CourseRepository;
import com.student.student_management.utils.StudentManagementUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    // -----------------------------------------
    // 1. Create Course - Success
    // -----------------------------------------

    @Test
    void createCourse_shouldCreateAndReturnCourse() {

        CourseRequest request = new CourseRequest();
        request.setCourseCode("JAVA101");
        request.setCourseName("Java Programming");
        request.setDescription("Core Java and OOP concepts");
        request.setDuration(30);

        when(courseRepository.existsByCourseCode("JAVA101"))
                .thenReturn(false);

        when(courseRepository.save(any(Course.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Course result = courseService.createCourse(request);

        assertNotNull(result);
        assertEquals("JAVA101", result.getCourseCode());
        assertEquals("Java Programming", result.getCourseName());
        assertEquals("Core Java and OOP concepts",
                result.getDescription());
        assertEquals(30, result.getDuration());

        verify(courseRepository, times(1))
                .existsByCourseCode("JAVA101");

        verify(courseRepository, times(1))
                .save(any(Course.class));
    }

    // -----------------------------------------
    // 2. Create Course - Duplicate Course Code
    // -----------------------------------------

    @Test
    void createCourse_shouldThrowExceptionForDuplicateCourseCode() {

        CourseRequest request = new CourseRequest();
        request.setCourseCode("JAVA101");
        request.setCourseName("Java Programming");

        when(courseRepository.existsByCourseCode("JAVA101"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> courseService.createCourse(request)
                );

        assertEquals(
                "Course code already exists",
                exception.getMessage()
        );

        verify(courseRepository, times(1))
                .existsByCourseCode("JAVA101");

        // Save should not be called for duplicate course code.
        verify(courseRepository, never())
                .save(any(Course.class));
    }

    // -----------------------------------------
    // 3. Get All Courses
    // -----------------------------------------

    @Test
    void getAllCourses_shouldReturnAllCourses() {

        Course course1 = new Course();
        course1.setId(1L);
        course1.setCourseCode("JAVA101");
        course1.setCourseName("Java Programming");

        Course course2 = new Course();
        course2.setId(2L);
        course2.setCourseCode("ANG101");
        course2.setCourseName("Angular Development");

        List<Course> courses = List.of(course1, course2);

        when(courseRepository.findAll())
                .thenReturn(courses);

        List<Course> result = courseService.getAllCourses();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("JAVA101", result.get(0).getCourseCode());
        assertEquals("ANG101", result.get(1).getCourseCode());

        verify(courseRepository, times(1)).findAll();
    }

    // -----------------------------------------
    // 4. Get Course By ID - Success
    // -----------------------------------------

    @Test
    void getCourseById_shouldReturnCourse() {

        Long courseId = 1L;

        Course course = new Course();
        course.setId(courseId);
        course.setCourseCode("JAVA101");
        course.setCourseName("Java Programming");

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(courseId);

        assertNotNull(result);
        assertEquals(courseId, result.getId());
        assertEquals("JAVA101", result.getCourseCode());
        assertEquals("Java Programming", result.getCourseName());

        verify(courseRepository, times(1))
                .findById(courseId);
    }

    // -----------------------------------------
    // 5. Get Course By ID - Not Found
    // -----------------------------------------

    @Test
    void getCourseById_shouldThrowExceptionWhenNotFound() {

        Long courseId = 100L;

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> courseService.getCourseById(courseId)
                );

        assertEquals(
                "Course not found: " + courseId,
                exception.getMessage()
        );

        verify(courseRepository, times(1))
                .findById(courseId);
    }

    // -----------------------------------------
    // 6. Update Course - Success
    // -----------------------------------------

    @Test
    void updateCourse_shouldUpdateAndSaveCourse() {

        Long courseId = 1L;

        Course existingCourse = new Course();
        existingCourse.setId(courseId);
        existingCourse.setCourseCode("JAVA101");
        existingCourse.setCourseName("Core Java");
        existingCourse.setDescription("Basic Java");
        existingCourse.setDuration(20);

        CourseRequest request = new CourseRequest();
        request.setCourseCode("JAVA201");
        request.setCourseName("Advanced Java");
        request.setDescription("Advanced Java concepts");
        request.setDuration(45);

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.of(existingCourse));

        when(courseRepository.save(any(Course.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Course result =
                courseService.updateCourse(courseId, request);

        assertNotNull(result);
        assertEquals("JAVA201", result.getCourseCode());
        assertEquals("Advanced Java", result.getCourseName());
        assertEquals("Advanced Java concepts",
                result.getDescription());
        assertEquals(45, result.getDuration());

        verify(courseRepository, times(1))
                .findById(courseId);

        verify(courseRepository, times(1))
                .save(existingCourse);
    }

    // -----------------------------------------
    // 7. Update Course - Not Found
    // -----------------------------------------

    @Test
    void updateCourse_shouldThrowExceptionWhenCourseNotFound() {

        Long courseId = 100L;

        CourseRequest request = new CourseRequest();
        request.setCourseCode("JAVA201");
        request.setCourseName("Advanced Java");

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.updateCourse(courseId, request)
        );

        verify(courseRepository, times(1))
                .findById(courseId);

        verify(courseRepository, never())
                .save(any(Course.class));
    }

    // -----------------------------------------
    // 8. Delete Course - Soft Delete
    // -----------------------------------------

    @Test
    void deleteCourse_shouldMarkCourseInactive() {

        Long courseId = 1L;

        Course course = new Course();
        course.setId(courseId);
        course.setCourseCode("JAVA101");
        course.setCourseName("Java Programming");
        course.setStatus(StudentManagementUtils.STATUS_ACTIVE);

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.of(course));

        when(courseRepository.save(any(Course.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        courseService.deleteCourse(courseId);

        assertEquals(
                StudentManagementUtils.STATUS_INACTIVE,
                course.getStatus()
        );

        verify(courseRepository, times(1))
                .findById(courseId);

        verify(courseRepository, times(1))
                .save(course);
    }

    // -----------------------------------------
    // 9. Delete Course - Not Found
    // -----------------------------------------

    @Test
    void deleteCourse_shouldThrowExceptionWhenCourseNotFound() {

        Long courseId = 100L;

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.deleteCourse(courseId)
        );

        verify(courseRepository, times(1))
                .findById(courseId);

        verify(courseRepository, never())
                .save(any(Course.class));
    }
}
