package me.didi.api.ess.services;

import me.didi.api.ess.entities.Grade;
import me.didi.api.ess.entities.Registration;
import me.didi.api.ess.entities.Subject;
import me.didi.api.ess.entities.pks.GradeId;
import me.didi.api.ess.enums.GradeType;
import me.didi.api.ess.exceptions.DataIntegrityViolationException;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.exceptions.EssException;
import me.didi.api.ess.repositories.GradeRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opentest4j.MultipleFailuresError;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static me.didi.api.ess.utils.Assertions.assertThrowsExceptionWithCorrectMessage;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Grade Service Tests")
@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    @Mock
    private GradeRepository repository;
    @Mock
    private RegistrationService registrationService;
    @Mock
    private SubjectService subjectService;
    @InjectMocks
    @Spy
    private GradeService service;

    private Grade grade;
    private ArgumentCaptor<Grade> gradeArgumentCaptor;

    private static Stream<Arguments> providedGrades() {
        return Stream.of(
                Arguments.of(GradeType.FINAL, GradeType.FINAL),
                Arguments.of(GradeType.PARTIAL, GradeType.PARTIAL),
                Arguments.of(GradeType.PARTIAL, GradeType.FINAL)
        );
    }

    private void assertGrade(Grade expected, Grade result) throws MultipleFailuresError {
        assertAll(
                "Assert that Expected Grade has been returned"
                , () -> assertNotNull(result)
                , () -> assertEquals(expected.getId(), result.getId())
                , () -> assertEquals(expected.getGradeType(), result.getGradeType())
                , () -> assertEquals(expected.getValue(), result.getValue())
        );
    }

    @BeforeEach
    void setup() {
        Registration registration = Instancio.create(Registration.class);
        Subject subject = Instancio.create(Subject.class);
        registration.getSubjects().add(subject);
        GradeId gradeId = new GradeId();
        gradeId.setRegistration(registration);
        gradeId.setSubject(subject);
        grade = Instancio.of(Grade.class)
                .set(field("id"), gradeId)
                .set(field("gradeType"), GradeType.FINAL)
                .create();
        gradeArgumentCaptor = ArgumentCaptor.forClass(Grade.class);
    }

    @Test
    @DisplayName("1. Save New Grade")
    void saveNewGrade() {
        String studentId = grade.getId().getRegistration().getId().getStudent().getId();
        String classId = grade.getId().getRegistration().getId().getClazz().getId();
        String subjectId = grade.getId().getSubject().getId();

        when(registrationService.findById(studentId, classId))
                .thenReturn(grade.getId().getRegistration());
        when(subjectService.findById(subjectId))
                .thenReturn(grade.getId().getSubject());

        doThrow(new EntityNotFoundException("")).when(service).findById(studentId, classId, subjectId);

        when(repository.save(any(Grade.class))).thenReturn(grade);

        Grade result = service.save(grade);

        verify(registrationService).findById(studentId, classId);
        verifyNoMoreInteractions(registrationService);
        verify(subjectService).findById(subjectId);
        verifyNoMoreInteractions(subjectService);
        verify(repository).save(grade);
        verifyNoMoreInteractions(repository);
        verify(service).save(grade);
        verify(service).findById(studentId, classId, subjectId);
        verifyNoMoreInteractions(service);

        assertGrade(grade, result);
    }

    @Test
    @DisplayName("1.1. Throw DataIntegrityViolationException When Subject is not within Registration scope")
    void shouldHasThrowDataIntegrityViolationExceptionWhenSubjectIsNotWithinRegistrationScope() {
        String studentId = grade.getId().getRegistration().getId().getStudent().getId();
        String classId = grade.getId().getRegistration().getId().getClazz().getId();
        String subjectId = grade.getId().getSubject().getId();

        grade.getId().getRegistration().getSubjects().remove(grade.getId().getSubject());

        when(registrationService.findById(studentId, classId))
                .thenReturn(grade.getId().getRegistration());
        when(subjectService.findById(subjectId))
                .thenReturn(grade.getId().getSubject());

        assertThrowsExceptionWithCorrectMessage(
                () -> service.save(grade),
                DataIntegrityViolationException.class,
                "The subject [" +
                        grade.getId().getSubject().getId() +
                        "] is not within the scope of registration"
        );

        verify(registrationService).findById(studentId, classId);
        verifyNoMoreInteractions(registrationService);
        verify(subjectService).findById(subjectId);
        verifyNoMoreInteractions(subjectService);
        verify(service).save(grade);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("1.2. Throw DataIntegrityViolationException when grade already has final grade and new grade is not final too")
    void shouldHasThrowDataIntegrityViolationExceptionWhenGradeAlreadyHasFinalGradeAndNewGradeIsNotFinal() {
        String studentId = grade.getId().getRegistration().getId().getStudent().getId();
        String classId = grade.getId().getRegistration().getId().getClazz().getId();
        String subjectId = grade.getId().getSubject().getId();

        Grade existingGrade = Instancio.of(Grade.class)
                .set(field("id"), grade.getId())
                .set(field("gradeType"), GradeType.FINAL)
                .create();

        grade.setGradeType(GradeType.PARTIAL);

        when(registrationService.findById(studentId, classId))
                .thenReturn(grade.getId().getRegistration());
        when(subjectService.findById(subjectId))
                .thenReturn(grade.getId().getSubject());
        doReturn(existingGrade).when(service).findById(studentId, classId, subjectId);


        assertThrowsExceptionWithCorrectMessage(
                () -> service.save(grade),
                DataIntegrityViolationException.class,
                "There is already a type of final grade informed"
        );

        verify(registrationService).findById(studentId, classId);
        verifyNoMoreInteractions(registrationService);
        verify(subjectService).findById(subjectId);
        verifyNoMoreInteractions(subjectService);
        verify(service).save(grade);
        verify(service).findById(studentId, classId, subjectId);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("1.3. Throw DataIntegrityViolationException when grade already has partial grade and new grade is ongoing type")
    void shouldHasThrowDataIntegrityViolationExceptionWhenGradeAlreadyHasPartialGradeAndNewGradeIsOngoing() {
        String studentId = grade.getId().getRegistration().getId().getStudent().getId();
        String classId = grade.getId().getRegistration().getId().getClazz().getId();
        String subjectId = grade.getId().getSubject().getId();

        Grade existingGrade = Instancio.of(Grade.class)
                .set(field("id"), grade.getId())
                .set(field("gradeType"), GradeType.PARTIAL)
                .create();

        grade.setGradeType(GradeType.ONGOING);

        when(registrationService.findById(studentId, classId))
                .thenReturn(grade.getId().getRegistration());
        when(subjectService.findById(subjectId))
                .thenReturn(grade.getId().getSubject());
        doReturn(existingGrade).when(service).findById(studentId, classId, subjectId);


        assertThrowsExceptionWithCorrectMessage(
                () -> service.save(grade),
                DataIntegrityViolationException.class,
                "There is already a type of partial grade informed"
        );

        verify(registrationService).findById(studentId, classId);
        verifyNoMoreInteractions(registrationService);
        verify(subjectService).findById(subjectId);
        verifyNoMoreInteractions(subjectService);
        verify(service).save(grade);
        verify(service).findById(studentId, classId, subjectId);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("1.4. Throw Generic Exception when checking grade and unknown exception occurs")
    void shouldHasGenericExceptionWhenCheckingGradeAndUnknownExceptionOccurs() {
        String studentId = grade.getId().getRegistration().getId().getStudent().getId();
        String classId = grade.getId().getRegistration().getId().getClazz().getId();
        String subjectId = grade.getId().getSubject().getId();

        when(registrationService.findById(studentId, classId))
                .thenReturn(grade.getId().getRegistration());
        when(subjectService.findById(subjectId))
                .thenReturn(grade.getId().getSubject());
        doThrow(new RuntimeException("")).when(service).findById(studentId, classId, subjectId);

        assertThrowsExceptionWithCorrectMessage(
                () -> service.save(grade),
                EssException.class,
                "Erro desconhecido :o"
        );

        verify(registrationService).findById(studentId, classId);
        verifyNoMoreInteractions(registrationService);
        verify(subjectService).findById(subjectId);
        verifyNoMoreInteractions(subjectService);
        verify(service).save(grade);
        verify(service).findById(studentId, classId, subjectId);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(repository);
    }

    @ParameterizedTest
    @MethodSource("providedGrades")
    @DisplayName("1.5. Save New Grade Even If Existing Grade And Validate Grade Level Not Throws Exception")
    void saveNewGradeEvenIfExistingGradeHasFinalGradeWhenActualGradeHasFinalGradeToo(
            GradeType existingGradeType, GradeType actualGradeType) {
        String studentId = grade.getId().getRegistration().getId().getStudent().getId();
        String classId = grade.getId().getRegistration().getId().getClazz().getId();
        String subjectId = grade.getId().getSubject().getId();

        Grade existingGrade = Instancio.of(Grade.class)
                .set(field("id"), grade.getId())
                .set(field("gradeType"), existingGradeType)
                .create();

        grade.setGradeType(actualGradeType);

        when(registrationService.findById(studentId, classId))
                .thenReturn(grade.getId().getRegistration());
        when(subjectService.findById(subjectId))
                .thenReturn(grade.getId().getSubject());
        doReturn(existingGrade).when(service).findById(studentId, classId, subjectId);
        when(repository.save(any(Grade.class))).thenReturn(grade);

        Grade result = service.save(grade);

        verify(registrationService).findById(studentId, classId);
        verifyNoMoreInteractions(registrationService);
        verify(subjectService).findById(subjectId);
        verifyNoMoreInteractions(subjectService);
        verify(repository).save(grade);
        verifyNoMoreInteractions(repository);
        verify(service).findById(studentId, classId, subjectId);
        verify(service).save(grade);
        verifyNoMoreInteractions(service);

        assertGrade(grade, result);
    }

    @Test
    @DisplayName("2. List Grades")
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(grade));

        List<Grade> result = service.findAll();

        assertAll("Assert that a list of the Gradees has been returned",
                () -> assertInstanceOf(List.class, result),
                () -> assertEquals(1, result.size()),
                () -> assertGrade(grade, result.getFirst())
        );

    }

    @Test
    @DisplayName("3. Find Grade by Id")
    void findById() {
        String studentId = grade.getId().getRegistration().getId().getStudent().getId();
        String classId = grade.getId().getRegistration().getId().getClazz().getId();
        String subjectId = grade.getId().getSubject().getId();

        when(registrationService.findById(studentId,classId))
                .thenReturn(grade.getId().getRegistration());
        when(subjectService.findById(subjectId))
                .thenReturn(grade.getId().getSubject());

        when(repository.findById(grade.getId())).thenReturn(Optional.of(grade));

        Grade result = service.findById(studentId, classId, subjectId);

        verify(registrationService).findById(studentId, classId);
        verifyNoMoreInteractions(registrationService);
        verify(subjectService).findById(subjectId);
        verifyNoMoreInteractions(subjectService);
        verify(service).findById(studentId, classId, subjectId);
        verifyNoMoreInteractions(service);
        verify(repository).findById(grade.getId());
        verifyNoMoreInteractions(repository);

        assertGrade(grade, result);

    }

    @Test
    @DisplayName("3.1. Throw Exception when try find Grade with Invalid Id")
    void throwsExceptionWhenTryFindByInvalidId() {
        GradeId invalidId = Instancio.create(GradeId.class);

        when(registrationService.findById(invalidId.getRegistration().getId().getStudent().getId(),
                invalidId.getRegistration().getId().getClazz().getId()))
                .thenReturn(invalidId.getRegistration());
        when(subjectService.findById(invalidId.getSubject().getId()))
                .thenReturn(invalidId.getSubject());

        when(repository.findById(any(GradeId.class))).thenReturn(Optional.empty());

        assertThrowsExceptionWithCorrectMessage(
                () -> service.findById(invalidId.getRegistration().getId().getStudent().getId(),
                        invalidId.getRegistration().getId().getClazz().getId(),
                        invalidId.getSubject().getId()),
                EntityNotFoundException.class,
                "Grade with Id [" +
                        invalidId +
                        "] Not Found!"
        );

    }


}