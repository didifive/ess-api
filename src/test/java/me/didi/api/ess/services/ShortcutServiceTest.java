package me.didi.api.ess.services;

import me.didi.api.ess.entities.Shortcut;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.ShortcutRepository;
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
@DisplayName("Shortcut Service Tests")
@ExtendWith(MockitoExtension.class)
class ShortcutServiceTest {

    @Mock
    private ShortcutRepository repository;
    @InjectMocks
    private ShortcutService service;

    private Shortcut shortcut;

    private void assertShortcut(Shortcut expected, Shortcut result) throws MultipleFailuresError {
        assertAll(
                "Assert that Expected Shortcut has been returned"
                , () -> assertNotNull(result)
                , () -> assertEquals(expected.getId(), result.getId())
                , () -> assertEquals(expected.getIcon(), result.getIcon())
                , () -> assertEquals(expected.getTitle(), result.getTitle())
                , () -> assertEquals(expected.getDescription(), result.getDescription())
                , () -> assertEquals(expected.getLink(), result.getLink())
        );
    }

    @BeforeEach
    void setup() {
        shortcut = Instancio.create(Shortcut.class);
    }

    @Test
    @DisplayName("1. Save New Shortcut")
    void saveNewShortcut() {
        when(repository.save(any(Shortcut.class))).thenReturn(shortcut);

        Shortcut result = service.save(shortcut);

        assertShortcut(shortcut, result);
    }

    @Test
    @DisplayName("2. List Shortcuts")
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(shortcut));

        List<Shortcut> result = service.findAll();

        assertAll("Assert that a list of the Shortcutes has been returned",
                () -> assertInstanceOf(List.class, result),
                () -> assertEquals(1, result.size()),
                () -> assertShortcut(shortcut, result.getFirst())
        );

    }

    @Test
    @DisplayName("3. Find Shortcut by Id")
    void findById() {
        Shortcut message = Instancio.create(Shortcut.class);
        String id = message.getId();

        when(repository.findById(any(String.class))).thenReturn(Optional.of(message));

        Shortcut result = service.findById(id);

        assertShortcut(message, result);

    }

    @Test
    @DisplayName("3.1. Throw Exception when try find Shortcut with Invalid Id")
    void throwsExceptionWhenTryFindByInvalidId() {
        String invalidId = UUID.randomUUID().toString();

        when(repository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrowsExceptionWithCorrectMessage(
                () -> service.findById(invalidId),
                EntityNotFoundException.class,
                "Shortcut with Id [" +
                        invalidId +
                        "] Not Found!"
        );

    }


}