package me.didi.api.ess.services;

import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.ClazzRepository;
import me.didi.api.ess.entities.Clazz;
import me.didi.api.ess.services.ClazzService;
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
@DisplayName("Clazz Service Tests")
@ExtendWith(MockitoExtension.class)
class ClazzServiceTest {

    @Mock
    private ClazzRepository repository;
    @InjectMocks
    private ClazzService service;

    private Clazz clazz;

    private void assertClazz(Clazz expected, Clazz result) throws MultipleFailuresError {
        assertAll(
                "Assert that Expected Class has been returned"
                , () -> assertNotNull(result)
                , () -> assertEquals(expected.getId(),      result.getId())
                , () -> assertEquals(expected.getName(),    result.getName())
                , () -> assertEquals(expected.getCourse(),  result.getCourse())
        );
    }

    @BeforeEach
    void setup() {
        clazz = Instancio.create(Clazz.class);
    }

    @Test
    @DisplayName("1. Save New Class")
    void saveNewClazz() {
        when(repository.save(any(Clazz.class))).thenReturn(clazz);

        Clazz result = service.save(clazz);

        assertClazz(clazz, result);
    }

    @Test
    @DisplayName("2. List Classes")
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(clazz));

        List<Clazz> result = service.findAll();

        assertAll("Assert that a list of the Classes has been returned",
                () -> assertInstanceOf(List.class, result),
                () -> assertEquals(1, result.size()),
                () -> assertClazz(clazz, result.getFirst())
        );

    }

    @Test
    @DisplayName("3. Find Class by Id")
    void findById() {
        String id = clazz.getId();

        when(repository.findById(any(String.class))).thenReturn(Optional.of(clazz));

        Clazz result = service.findById(id);

        assertClazz(clazz, result);

    }

    @Test
    @DisplayName("3.1. Throw Exception when try find Class with Invalid Id")
    void throwsExceptionWhenTryFindByInvalidId() {
        String invalidId = UUID.randomUUID().toString();

        when(repository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrowsExceptionWithCorrectMessage(
                () -> service.findById(invalidId),
                EntityNotFoundException.class,
                "Class with Id [" +
                        invalidId +
                        "] Not Found!"
        );

    }


}