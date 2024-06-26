package me.didi.api.ess.entities;

import me.didi.api.ess.entities.pks.RegistrationId;
import me.didi.api.ess.enums.GradeType;
import me.didi.api.ess.enums.RegistrationStatus;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static me.didi.api.ess.entities.Registration.PASSING_SCORE;
import static me.didi.api.ess.utils.Assertions.assertThrowsExceptionWithCorrectMessage;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Registration Entity Tests")
class RegistrationTest {

    public static final String PASSED_END_DATE = "PassedEndDate";
    public static final String PASSED_RECOVERY_DATE = "PassedRecoveryDate";
    public static final String HAS_ONGOING_GRADE = "HasOngoingGrade";
    public static final String HAS_PARTIAL_GRADE = "HasPartialGrade";
    public static final String HAS_PASSING_SCORE = "HasPassingScore";
    public static final String NO_PASSING_SCORE = "NoPassingScore";
    private Registration registration;

    @BeforeEach
    void setup(TestInfo info) {
        Set<Grade> grades = new HashSet<>();

        Set<Subject> subjects = Instancio.stream(Subject.class).limit(10).collect(Collectors.toSet());
        Clazz clazz = Instancio.of(Clazz.class)
                .set(field("initDate"), LocalDate.now().minusMonths(1))
                .set(field("recoveryDate")
                        , info.getTags().stream().anyMatch(t -> t.equals(PASSED_RECOVERY_DATE)) ?
                                LocalDate.now().minusDays(2) :
                                LocalDate.now().plusDays(3))
                .set(field("endDate")
                        , info.getTags().stream().anyMatch(t -> t.equals(PASSED_END_DATE)) ?
                                LocalDate.now().minusDays(1) :
                                LocalDate.now().plusDays(5))
                .create();
        RegistrationId registrationId = Instancio.of(RegistrationId.class)
                .set(field("clazz"), clazz)
                .create();
        registration = Instancio.of(Registration.class)
                .set(field("id"), registrationId)
                .set(field("subjects"), subjects)
                .create();
        subjects.forEach(subject -> {
            if (info.getTags().stream().anyMatch(t -> t.equals(HAS_ONGOING_GRADE))) {
                if (grades.stream().anyMatch(g -> g.getGradeType().equals(GradeType.ONGOING))) {
                    grades.add(new Grade(registration, subject, GradeType.FINAL, new BigDecimal(10)));
                } else {
                    grades.add(new Grade(registration, subject, GradeType.ONGOING, new BigDecimal(0)));
                }
            } else if (info.getTags().stream().anyMatch(t -> t.equals(HAS_PARTIAL_GRADE))) {
                if (grades.stream().anyMatch(g -> g.getGradeType().equals(GradeType.PARTIAL))) {
                    grades.add(new Grade(registration, subject, GradeType.FINAL, new BigDecimal(10)));
                } else {
                    grades.add(new Grade(registration, subject, GradeType.PARTIAL, new BigDecimal(0)));
                }
            } else if (info.getTags().stream().anyMatch(t -> t.equals(NO_PASSING_SCORE))) {
                if (grades.stream().anyMatch(g -> g.getValue().compareTo(PASSING_SCORE) < 0)) {
                    grades.add(new Grade(registration, subject, GradeType.FINAL, new BigDecimal(10)));
                } else {
                    grades.add(new Grade(registration, subject, GradeType.FINAL, new BigDecimal(5)));
                }
            } else {
                grades.add(new Grade(registration, subject, GradeType.FINAL, new BigDecimal(10)));
            }
        });
        registration.setGrades(grades);
    }


    @Test
    @DisplayName("0. Status Ongoing When Grades Is Null")
    void courseStatusOngoingWhenGradesIsNull() {
        registration.setGrades(null);
        RegistrationStatus result = registration.status();

        assertEquals(RegistrationStatus.ONGOING, result);
    }

    @Test
    @DisplayName("01. Status Ongoing When No Passed Recory and End Date")
    void courseStatusOngoingWhenNoPassedRecoveryDateAndNoPassedEndDate() {
        RegistrationStatus result = registration.status();

        assertEquals(RegistrationStatus.ONGOING, result);
    }

    @Test
    @DisplayName("02. Status Ongoing When Has Ongoing Grade")
    @Tags(value = {@Tag(value = PASSED_END_DATE),
            @Tag(value = HAS_ONGOING_GRADE)})
    void courseStatusOngoingWhenPassedEndDateAndHasOngoingGrade() {
        RegistrationStatus result = registration.status();

        assertEquals(RegistrationStatus.ONGOING, result);
    }

    @Test
    @DisplayName("03. Status Ongoing When Has Partial Grade")
    @Tags(value = {@Tag(value = PASSED_END_DATE),
            @Tag(value = HAS_PARTIAL_GRADE)})
    void courseStatusOngoingWhenPassedEndDateAndHasPartialGrade() {
        RegistrationStatus result = registration.status();

        assertEquals(RegistrationStatus.ONGOING, result);
    }

    @Test
    @DisplayName("04. Status Ongoing When Subjects Misses")
    @Tags(value = {@Tag(value = PASSED_RECOVERY_DATE),
            @Tag(value = NO_PASSING_SCORE)})
    void courseStatusOngoingWhenGradesSubjectsIsLowerThanCourseSubjects() {
        Set<Grade> fewGrades = registration.getGrades();

        fewGrades.remove(fewGrades.stream().findAny().orElseThrow(IllegalAccessError::new));

        registration.setGrades(fewGrades);

        RegistrationStatus result = registration.status();

        assertEquals(RegistrationStatus.ONGOING, result);
    }

    @Test
    @DisplayName("05. Status Approved When Passed End Date and Has Passing Score")
    @Tags(value = {@Tag(value = PASSED_END_DATE),
            @Tag(value = HAS_PASSING_SCORE)})
    void courseStatusApprovedWhenPassedEndDateAndHasPassingScore() {
        RegistrationStatus result = registration.status();

        assertEquals(RegistrationStatus.APPROVED, result);
    }

    @Test
    @DisplayName("06. Status Disapproved When Passed End Date and No Passing Score")
    @Tags(value = {@Tag(value = PASSED_END_DATE),
            @Tag(value = NO_PASSING_SCORE)})
    void courseStatusDisapprovedWhenPassedEndDateAndNoPassingScore() {
        RegistrationStatus result = registration.status();

        assertEquals(RegistrationStatus.DISAPPROVED, result);
    }


    @Test
    @DisplayName("07. Status Approved When Passed Recovery Date and Has Passing Score")
    @Tags(value = {@Tag(value = PASSED_RECOVERY_DATE),
            @Tag(value = HAS_PASSING_SCORE)})
    void courseStatusRecoveryWhenPassedRecoveryDateAndHasPassingScore() {
        RegistrationStatus result = registration.status();

        assertEquals(RegistrationStatus.APPROVED, result);
    }

    @Test
    @DisplayName("08. Status Recovery When Passed Recovery Date and No Passing Score")
    @Tags(value = {@Tag(value = PASSED_RECOVERY_DATE),
            @Tag(value = NO_PASSING_SCORE)})
    void courseStatusRecoveryWhenPassedRecoveryDateAndHasNoPassingScore() {
        RegistrationStatus result = registration.status();

        assertEquals(RegistrationStatus.RECOVERY, result);
    }

    @Test
    @DisplayName("09. Illegal Argument Exception When Wrong Event is Used")
    @Tags(value = {@Tag(value = PASSED_END_DATE),
            @Tag(value = NO_PASSING_SCORE)})
    void IllegalArgumentExceptionWhenWrongEventIsUsed() {
        assertThrowsExceptionWithCorrectMessage(
                () -> ReflectionTestUtils.invokeMethod(registration,
                        "getStatusAfterEventDate",
                        registration.getGrades(),
                        "WrongEvent"),
                IllegalArgumentException.class,
                "Argument event [" +
                        "WrongEvent" +
                        "] in getStatusAfterEventDate method is invalid!"
        );
    }
}