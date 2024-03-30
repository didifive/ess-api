package me.didi.api.ess.services;

import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.SubjectRepository;
import me.didi.api.ess.entities.Subject;
import me.didi.api.ess.services.SubjectService;
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
@DisplayName("Subject Service Tests")
@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository repository;
    @InjectMocks
    private SubjectService service;

    private Subject subject;

    private void assertSubject(Subject expected, Subject result) throws MultipleFailuresError {
        assertAll(
                "Assert that Expected Subject has been returned"
                , () -> assertNotNull(result)
                , () -> assertEquals(expected.getId(),          result.getId())
                , () -> assertEquals(expected.getIcon(),        result.getIcon())
                , () -> assertEquals(expected.getTitle(),       result.getTitle())
                , () -> assertEquals(expected.getDescription(), result.getDescription())
        );
    }

    @BeforeEach
    void setup() {
        subject = Instancio.create(Subject.class);
    }

    @Test
    @DisplayName("1. Save New Subject")
    void saveNewSubject() {
        when(repository.save(any(Subject.class))).thenReturn(subject);

        Subject result = service.save(subject);

        assertSubject(subject, result);
    }

    @Test
    @DisplayName("2. List Subjectes")
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(subject));

        List<Subject> result = service.findAll();

        assertAll("Assert that a list of the Subjectes has been returned",
                () -> assertInstanceOf(List.class, result),
                () -> assertEquals(1, result.size()),
                () -> assertSubject(subject, result.getFirst())
        );

    }

    @Test
    @DisplayName("3. Find Subject by Id")
    void findById() {
        Subject subject = Instancio.create(Subject.class);
        String id = subject.getId();

        when(repository.findById(any(String.class))).thenReturn(Optional.of(subject));

        Subject result = service.findById(id);

        assertSubject(subject, result);

    }

    @Test
    @DisplayName("3.1. Throw Exception when try find Subject with Invalid Id")
    void throwsExceptionWhenTryFindByInvalidId() {
        String invalidId = UUID.randomUUID().toString();

        when(repository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrowsExceptionWithCorrectMessage(
                () -> service.findById(invalidId),
                EntityNotFoundException.class,
                "Subject with Id [" +
                        invalidId +
                        "] Not Found!"
        );

    }


}