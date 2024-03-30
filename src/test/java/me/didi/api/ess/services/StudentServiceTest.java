package me.didi.api.ess.services;

import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.StudentRepository;
import me.didi.api.ess.entities.Student;
import me.didi.api.ess.services.StudentService;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opentest4j.MultipleFailuresError;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static me.didi.api.ess.utils.Assertions.assertThrowsExceptionWithCorrectMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Student Service Tests")
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;
    @InjectMocks
    private StudentService service;

    private Student student;

    private void assertStudent(Student expected, Student result) throws MultipleFailuresError {
        assertAll(
                "Assert that Expected Student has been returned"
                , () -> assertNotNull(result)
                , () -> assertEquals(expected.getId(), result.getId())
                , () -> assertEquals(expected.getGivenName(), result.getGivenName())
                , () -> assertEquals(expected.getFamilyName(), result.getFamilyName())
                , () -> assertEquals(expected.getGuardian(), result.getGuardian())
                , () -> assertEquals(expected.getPhoto(), result.getPhoto())
        );
    }

    @BeforeEach
    void setup() {
        student = Instancio.create(Student.class);
    }

    @Test
    @DisplayName("1. Save New Student")
    void saveNewStudent() {
        when(repository.save(any(Student.class))).thenReturn(student);

        Student result = service.save(student);

        assertStudent(student, result);
    }

    @Test
    @DisplayName("2. List Students")
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(student));

        List<Student> result = service.findAll();

        assertAll("Assert that a list of the Students has been returned",
                () -> assertInstanceOf(List.class, result),
                () -> assertEquals(1, result.size()),
                () -> assertStudent(student, result.getFirst())
        );

    }

    @Test
    @DisplayName("3. Find Student by Id")
    void findById() {
        String id = student.getId();

        when(repository.findById(any(String.class))).thenReturn(Optional.of(student));

        Student result = service.findById(id);

        assertStudent(student, result);

    }

    @Test
    @DisplayName("3.1. Throw Exception when try find Student with Invalid Id")
    void throwsExceptionWhenTryFindByInvalidId() {
        String invalidId = UUID.randomUUID().toString();

        when(repository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrowsExceptionWithCorrectMessage(
                () -> service.findById(invalidId),
                EntityNotFoundException.class,
                "Student with Id [" +
                        invalidId +
                        "] Not Found!"
        );

    }


}