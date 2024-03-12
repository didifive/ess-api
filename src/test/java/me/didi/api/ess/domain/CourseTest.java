package me.didi.api.ess.domain;

import me.didi.api.ess.domain.enums.CourseStatus;
import me.didi.api.ess.domain.enums.GradeType;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static me.didi.api.ess.domain.Course.PASSING_SCORE;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseTest {

    public static final String PASSED_END_DATE = "PassedEndDate";
    public static final String PASSED_RECOVERY_DATE = "PassedRecoveryDate";
    public static final String HAS_ONGOING_GRADE = "HasOngoingGrade";
    public static final String HAS_PARTIAL_GRADE = "HasPartialGrade";
    public static final String HAS_PASSING_SCORE = "HasPassingScore";
    public static final String NO_PASSING_SCORE = "NoPassingScore";
    public static final String GRADES_SUBJECTS_IS_LOWER_THAN_COURSE_SUBJECTS = "GradesSubjectsIsLowerThanCourseSubjects";
    private final Set<Grade> grades = new HashSet<>();
    private Student student;
    private Course course;

    @BeforeEach
    void setup(TestInfo info) {
        Set<Subject> subjects = Instancio.stream(Subject.class).limit(10).collect(Collectors.toSet());
        student = Instancio.create(Student.class);
        course = Instancio.of(Course.class)
                .set(field("initDate"), LocalDate.now().minusMonths(1))
                .set(field("recoveryDate")
                        , info.getTags().stream().anyMatch(t -> t.equals(PASSED_RECOVERY_DATE)) ?
                                LocalDate.now().minusDays(2) :
                                LocalDate.now().plusDays(3))
                .set(field("endDate")
                        , info.getTags().stream().anyMatch(t -> t.equals(PASSED_END_DATE)) ?
                                LocalDate.now().minusDays(1) :
                                LocalDate.now().plusDays(5))
                .set(field("subjects"), subjects)
                .create();
        course.subjects().addAll(subjects);
        subjects.forEach(subject -> {
            if (info.getTags().stream().anyMatch(t -> t.equals(HAS_ONGOING_GRADE))) {
                if (grades.stream().anyMatch(g -> g.type().equals(GradeType.ONGOING))) {
                    grades.add(new Grade(student, course, subject, GradeType.FINAL, new BigDecimal(10)));
                } else {
                    grades.add(new Grade(student, course, subject, GradeType.ONGOING, new BigDecimal(0)));
                }
            } else if (info.getTags().stream().anyMatch(t -> t.equals(HAS_PARTIAL_GRADE))) {
                if (grades.stream().anyMatch(g -> g.type().equals(GradeType.PARTIAL))) {
                    grades.add(new Grade(student, course, subject, GradeType.FINAL, new BigDecimal(10)));
                } else {
                    grades.add(new Grade(student, course, subject, GradeType.PARTIAL, new BigDecimal(0)));
                }
            } else if (info.getTags().stream().anyMatch(t -> t.equals(NO_PASSING_SCORE))) {
                if (grades.stream().anyMatch(g -> g.value().compareTo(PASSING_SCORE) < 0)) {
                    grades.add(new Grade(student, course, subject, GradeType.FINAL, new BigDecimal(10)));
                } else {
                    grades.add(new Grade(student, course, subject, GradeType.FINAL, new BigDecimal(5)));
                }
            } else {
                grades.add(new Grade(student, course, subject, GradeType.FINAL, new BigDecimal(10)));
            }

        });

        if (info.getTags().stream().anyMatch(t -> t.equals(GRADES_SUBJECTS_IS_LOWER_THAN_COURSE_SUBJECTS))) {
            grades.remove(grades.stream().findFirst().orElseThrow(IllegalAccessError::new));
        }

    }

    @Test
    void courseStatusOngoingWhenNoPassedRecoveryDateOrEndDate() {
        CourseStatus result = course.status(student, grades);

        assertEquals(CourseStatus.ONGOING, result);
    }

    @Test
    @Tags(value = {@Tag(value = PASSED_END_DATE),
            @Tag(value = HAS_ONGOING_GRADE)})
    void courseStatusOngoingWhenPassedEndDateAndHasOngoingGrade() {
        CourseStatus result = course.status(student, grades);

        assertEquals(CourseStatus.ONGOING, result);
    }

    @Test
    @Tags(value = {@Tag(value = PASSED_END_DATE),
            @Tag(value = HAS_PARTIAL_GRADE)})
    void courseStatusOngoingWhenPassedEndDateAndHasPartialGrade() {
        CourseStatus result = course.status(student, grades);

        assertEquals(CourseStatus.ONGOING, result);
    }

    @Test
    @Tags(value = {@Tag(value = PASSED_END_DATE),
            @Tag(value = HAS_PASSING_SCORE)})
    void courseStatusApprovedWhenPassedEndDateAndHasPassingScore() {
        CourseStatus result = course.status(student, grades);

        assertEquals(CourseStatus.APPROVED, result);
    }

    @Test
    @Tags(value = {@Tag(value = PASSED_END_DATE),
            @Tag(value = NO_PASSING_SCORE)})
    void courseStatusDisapprovedWhenPassedEndDateAndNoPassingScore() {
        CourseStatus result = course.status(student, grades);

        assertEquals(CourseStatus.DISAPPROVED, result);
    }


    @Test
    @Tags(value = {@Tag(value = PASSED_RECOVERY_DATE),
            @Tag(value = HAS_PASSING_SCORE)})
    void courseStatusRecoveryWhenPassedRecoveryDateAndHasPassingScore() {
        CourseStatus result = course.status(student, grades);

        assertEquals(CourseStatus.APPROVED, result);
    }

    @Test
    @Tags(value = {@Tag(value = PASSED_RECOVERY_DATE),
            @Tag(value = NO_PASSING_SCORE)})
    void courseStatusRecoveryWhenPassedRecoveryDateAndHasNoPassingScore() {
        CourseStatus result = course.status(student, grades);

        assertEquals(CourseStatus.RECOVERY, result);
    }

    @Test
    @Tags(value = {@Tag(value = GRADES_SUBJECTS_IS_LOWER_THAN_COURSE_SUBJECTS),
            @Tag(value = PASSED_RECOVERY_DATE),
            @Tag(value = NO_PASSING_SCORE)})
    void courseStatusOngoingWhenGradesSubjectsIsLowerThanCourseSubjects() {
        CourseStatus result = course.status(student, grades);

        assertEquals(CourseStatus.ONGOING, result);
    }
}