package me.didi.api.ess.domain;

import me.didi.api.ess.domain.enums.CourseStatus;
import me.didi.api.ess.domain.enums.Frequency;
import me.didi.api.ess.domain.enums.GradeType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record Course(UUID id,
                     String name,
                     Frequency frequency,
                     String period,
                     LocalDate initDate,
                     LocalDate recoveryDate,
                     LocalDate endDate,
                     Set<Subject> subjects,
                     Set<Message> messages,
                     Set<News> news,
                     Set<Shortcut> shortcuts
) {

    public static final String END_DATE = "endDate";
    public static final String RECOVERY_DATE = "recoveryDate";
    public static final BigDecimal PASSING_SCORE = new BigDecimal("7.00");

    public static Course createNew(String name,
                                   Frequency frequency,
                                   String period,
                                   LocalDate initDate,
                                   LocalDate recoveryDate,
                                   LocalDate endDate,
                                   Set<Subject> subjects,
                                   Set<Message> messages,
                                   Set<News> news,
                                   Set<Shortcut> shortcuts) {
        return new Course(
                UUID.randomUUID(),
                name,
                frequency,
                period,
                initDate,
                recoveryDate,
                endDate,
                subjects,
                messages,
                news,
                shortcuts
        );
    }

    private static boolean isLowScore(Grade g) {
        return g.value().compareTo(PASSING_SCORE) < 0;
    }

    public CourseStatus status(Student student, Set<Grade> grades) {
        Set<Grade> studentSubjectsGrades = grades.stream()
                .filter(grade -> grade.student().equals(student))
                .filter(grade -> this.subjects().contains(grade.subject()))
                .collect(Collectors.toSet());

        Set<Subject> gradesSubjects = studentSubjectsGrades.stream().map(Grade::subject).collect(Collectors.toSet());
        if (!gradesSubjects.containsAll(this.subjects)) {
            return CourseStatus.ONGOING;
        }

        if (this.endDate.isBefore(LocalDate.now())) {
            return this.getStatusAfterEventDate(studentSubjectsGrades, END_DATE);
        }

        if (this.recoveryDate.isBefore(LocalDate.now())) {
            return this.getStatusAfterEventDate(studentSubjectsGrades, RECOVERY_DATE);
        }

        return CourseStatus.ONGOING;

    }

    private CourseStatus getStatusAfterEventDate(Set<Grade> grades, String event) {
        Set<Student> students = grades.stream()
                .map(Grade::student)
                .collect(Collectors.toSet());
        if (students.size() > 1) {
            throw new IllegalArgumentException();
        }

        Student student = students.stream()
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        if (hasOngoingOrPartialGrade(student, grades)) {
            return CourseStatus.ONGOING;
        }

        if (hasAnyLowGrade(student, grades)) {
            switch (event) {
                case END_DATE -> {
                    return CourseStatus.DISAPPROVED;
                }
                case RECOVERY_DATE -> {
                    return CourseStatus.RECOVERY;
                }
                default -> throw new IllegalArgumentException();
            }
        }

        return CourseStatus.APPROVED;
    }

    private boolean hasOngoingOrPartialGrade(Student student, Set<Grade> grades) {
        return grades.stream()
                .anyMatch(g -> g.student().equals(student)
                        && (g.type().equals(GradeType.ONGOING) || g.type().equals(GradeType.PARTIAL)));
    }

    private boolean hasAnyLowGrade(Student student, Set<Grade> grades) {
        return grades.stream()
                .filter(g -> g.student().equals(student) && g.type().equals(GradeType.FINAL))
                .anyMatch(Course::isLowScore);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(name, course.name) && frequency == course.frequency && Objects.equals(period, course.period) && Objects.equals(initDate, course.initDate) && Objects.equals(recoveryDate, course.recoveryDate) && Objects.equals(endDate, course.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, frequency, period, initDate, recoveryDate, endDate);
    }
}
