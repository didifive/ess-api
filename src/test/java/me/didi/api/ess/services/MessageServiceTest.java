package me.didi.api.ess.services;

import me.didi.api.ess.entities.Message;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.MessageRepository;
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
@DisplayName("Message Service Tests")
@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository repository;
    @InjectMocks
    private MessageService service;

    private Message message;

    private void assertMessage(Message expected, Message result) throws MultipleFailuresError {
        assertAll(
                "Assert that Expected Message has been returned"
                , () -> assertNotNull(result)
                , () -> assertEquals(expected.getId(), result.getId())
                , () -> assertEquals(expected.getIcon(), result.getIcon())
                , () -> assertEquals(expected.getTitle(), result.getTitle())
                , () -> assertEquals(expected.getDescription(), result.getDescription())
                , () -> assertEquals(expected.getDateTime(), result.getDateTime())
        );
    }

    @BeforeEach
    void setup() {
        message = Instancio.create(Message.class);
    }

    @Test
    @DisplayName("1. Save New Message")
    void saveNewMessage() {
        when(repository.save(any(Message.class))).thenReturn(message);

        Message result = service.save(message);

        assertMessage(message, result);
    }

    @Test
    @DisplayName("2. List Messagees")
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(message));

        List<Message> result = service.findAll();

        assertAll("Assert that a list of the Messagees has been returned",
                () -> assertInstanceOf(List.class, result),
                () -> assertEquals(1, result.size()),
                () -> assertMessage(message, result.getFirst())
        );

    }

    @Test
    @DisplayName("3. Find Message by Id")
    void findById() {
        String id = message.getId();

        when(repository.findById(any(String.class))).thenReturn(Optional.of(message));

        Message result = service.findById(id);

        assertMessage(message, result);

    }

    @Test
    @DisplayName("3.1. Throw Exception when try find Message with Invalid Id")
    void throwsExceptionWhenTryFindByInvalidId() {
        String invalidId = UUID.randomUUID().toString();

        when(repository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrowsExceptionWithCorrectMessage(
                () -> service.findById(invalidId),
                EntityNotFoundException.class,
                "Message with Id [" +
                        invalidId +
                        "] Not Found!"
        );

    }


}