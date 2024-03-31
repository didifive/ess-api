package me.didi.api.ess.services;

import me.didi.api.ess.entities.Course;
import me.didi.api.ess.entities.Registration;
import me.didi.api.ess.entities.Subject;
import me.didi.api.ess.entities.pks.RegistrationId;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.RegistrationRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Registration Service Tests")
@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private RegistrationRepository repository;
    @Mock
    private StudentService studentService;
    @Mock
    private ClazzService clazzService;
    @Mock
    private SubjectService subjectService;
    @InjectMocks
    private RegistrationService service;

    private Registration registration;
    private ArgumentCaptor<Registration> registrationArgumentCaptor;

    private void assertRegistration(Registration expected, Registration result) throws MultipleFailuresError {
        assertAll(
                "Assert that Expected Registration has been returned"
                , () -> assertNotNull(result)
                , () -> assertEquals(expected.getId(),                  result.getId())
                , () -> assertEquals(expected.getSubjects(),            result.getSubjects())
                , () -> assertEquals(expected.getRegistrationDate(),    result.getRegistrationDate())
        );
    }

    @BeforeEach
    void setup() {
        registration = Instancio.create(Registration.class);
        registrationArgumentCaptor = ArgumentCaptor.forClass(Registration.class);

    }

    @Test
    @DisplayName("1. Save New Registration")
    void saveNewRegistration() {
        int numberOfSubjects = registration.getSubjects().size();

        when(studentService.findById(registration.getId().getStudent().getId()))
                .thenReturn(registration.getId().getStudent());
        when(clazzService.findById(registration.getId().getClazz().getId()))
                .thenReturn(registration.getId().getClazz());
        when(subjectService.findById(anyString())).thenReturn(
                registration.getSubjects().stream().findFirst().orElse(new Subject()),
                registration.getSubjects().toArray(new Subject[registration.getSubjects().size()-1])
        );
        when(repository.save(any(Registration.class))).thenReturn(registration);

        Registration result = service.save(registration);

        verify(studentService).findById(registration.getId().getStudent().getId());
        verifyNoMoreInteractions(studentService);
        verify(clazzService).findById(registration.getId().getClazz().getId());
        verifyNoMoreInteractions(clazzService);
        verify(subjectService, times(numberOfSubjects)).findById(anyString());
        verifyNoMoreInteractions(subjectService);
        verify(repository).save(registration);
        verifyNoMoreInteractions(repository);

        assertRegistration(registration, result);
    }

    @Test
    @DisplayName("2. List Registrations")
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(registration));

        List<Registration> result = service.findAll();

        assertAll("Assert that a list of the Registrationes has been returned",
                () -> assertInstanceOf(List.class, result),
                () -> assertEquals(1, result.size()),
                () -> assertRegistration(registration, result.getFirst())
        );

    }

    @Test
    @DisplayName("3. Find Registration by Id")
    void findById() {
        RegistrationId id = registration.getId();

        when(studentService.findById(registration.getId().getStudent().getId()))
                .thenReturn(registration.getId().getStudent());
        when(clazzService.findById(registration.getId().getClazz().getId()))
                .thenReturn(registration.getId().getClazz());

        when(repository.findById(id)).thenReturn(Optional.of(registration));

        Registration result = service.findById(id.getStudent().getId()
                , id.getClazz().getId());

        verify(studentService).findById(registration.getId().getStudent().getId());
        verifyNoMoreInteractions(studentService);
        verify(clazzService).findById(registration.getId().getClazz().getId());
        verifyNoMoreInteractions(clazzService);

        assertRegistration(registration, result);

    }

    @Test
    @DisplayName("3.1. Throw Exception when try find Registration with Invalid Id")
    void throwsExceptionWhenTryFindByInvalidId() {
        RegistrationId invalidId = Instancio.create(RegistrationId.class);

        when(studentService.findById(invalidId.getStudent().getId()))
                .thenReturn(invalidId.getStudent());
        when(clazzService.findById(invalidId.getClazz().getId()))
                .thenReturn(invalidId.getClazz());

        when(repository.findById(any(RegistrationId.class))).thenReturn(Optional.empty());

        assertThrowsExceptionWithCorrectMessage(
                () -> service.findById(invalidId.getStudent().getId()
                        , invalidId.getClazz().getId()),
                EntityNotFoundException.class,
                "Registration with Id [" +
                        invalidId +
                        "] Not Found!"
        );

    }


}