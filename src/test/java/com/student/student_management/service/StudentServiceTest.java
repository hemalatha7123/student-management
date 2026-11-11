package com.student.student_management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.student.student_management.dto.StudentRequest;
import com.student.student_management.entity.Course;
import com.student.student_management.entity.Student;
import com.student.student_management.exception.ResourceNotFoundException;
import com.student.student_management.repository.StudentRepository;
import com.student.student_management.utils.StudentManagementUtils;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    @Test
    void shouldCreateStudent() {

        StudentRequest request = new StudentRequest();

        request.setFirstName("Rahul");
        request.setLastName("Sharma");
        request.setEmail("rahul@gmail.com");

        when(repository.existsByEmail(request.getEmail()))
            .thenReturn(false);

        when(repository.save(any(Student.class)))
            .thenAnswer(invocation -> {
                Student student = invocation.getArgument(0);
                student.setId(1L);
                return student;
            });

        Student result = service.createStudent(request);

        assertNotNull(result);
        assertEquals("Rahul", result.getFirstName());
        assertEquals(1L, result.getId());

        verify(repository).save(any(Student.class));
    }
    
 // -----------------------------------------
    // 1. Get Student By ID - Success
    // -----------------------------------------

    @Test
    void getStudentById_shouldReturnStudent() {

        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);
        student.setFirstName("John");
        student.setLastName("Smith");
        student.setEmail("john@gmail.com");

        when(repository.findById(studentId))
                .thenReturn(Optional.of(student));

        Student result =
                service.getStudentById(studentId);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Smith", result.getLastName());

        verify(repository, times(1))
                .findById(studentId);
    }

    // -----------------------------------------
    // 2. Get Student By ID - Student Not Found
    // -----------------------------------------

    @Test
    void getStudentById_shouldThrowExceptionWhenNotFound() {

        Long studentId = 100L;

        when(repository.findById(studentId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.getStudentById(studentId)
                );

        assertEquals(
                "Student not found: " + studentId,
                exception.getMessage()
        );

        verify(repository, times(1))
                .findById(studentId);
    }

    // -----------------------------------------
    // 3. Update Student - Success
    // -----------------------------------------

    @Test
    void updateStudent_shouldUpdateAndSaveStudent() {

        Long studentId = 1L;

        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setFirstName("John");
        existingStudent.setLastName("Smith");
        existingStudent.setEmail("john@gmail.com");
        existingStudent.setPhone("1111111111");

        StudentRequest request = new StudentRequest();
        request.setFirstName("David");
        request.setLastName("Brown");
        request.setEmail("david@gmail.com");
        request.setPhone("2222222222");

        when(repository.findById(studentId))
                .thenReturn(Optional.of(existingStudent));

        when(repository.save(any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Student result =
                service.updateStudent(studentId, request);

        assertNotNull(result);
        assertEquals("David", result.getFirstName());
        assertEquals("Brown", result.getLastName());
        assertEquals("david@gmail.com", result.getEmail());
        assertEquals("2222222222", result.getPhone());

        verify(repository, times(1))
                .findById(studentId);

        verify(repository, times(1))
                .save(existingStudent);
    }

    // -----------------------------------------
    // 4. Delete Student - Mark as INACTIVE
    // -----------------------------------------

    @Test
    void deleteStudent_shouldMarkStudentInactive() {

        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);
        student.setFirstName("John");
        student.setLastName("Smith");

        // Student has no course enrollments.
        assertTrue(student.getCourses().isEmpty());

        when(repository.findById(studentId))
                .thenReturn(Optional.of(student));

        when(repository.save(any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.deleteStudent(studentId);

        assertEquals(
                StudentManagementUtils.STATUS_INACTIVE,
                student.getStatus()
        );

        verify(repository, times(1))
                .findById(studentId);

        verify(repository, times(1))
                .save(student);
    }

    // -----------------------------------------
    // 5. Delete Student - Student Has Courses
    // -----------------------------------------

    @Test
    void deleteStudent_shouldThrowExceptionWhenStudentHasCourses() {

        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);
        student.setFirstName("John");

        // Add an enrollment.
        student.getCourses().add(new Course());

        when(repository.findById(studentId))
                .thenReturn(Optional.of(student));

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> service.deleteStudent(studentId)
                );

        assertEquals(
                "Remove student enrollments before deletion",
                exception.getMessage()
        );

        verify(repository, times(1))
                .findById(studentId);

        // Student must not be saved.
        verify(repository, never())
                .save(any(Student.class));
    }
}