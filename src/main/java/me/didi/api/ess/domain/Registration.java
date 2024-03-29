package me.didi.api.ess.domain;

import me.didi.api.ess.domain.enums.RegistrationStatus;
import me.didi.api.ess.domain.enums.GradeType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record Registration(Student student,
                           Clazz clazz,
                           Set<Subject> subjects,
                           LocalDate registrationDate,
                           LocalDate recoveryDate,
                           LocalDate endDate
) {

    public static final String END_DATE = "endDate";
    public static final String RECOVERY_DATE = "recoveryDate";
    public static final BigDecimal PASSING_SCORE = new BigDecimal("7.00");
    private static boolean isLowScore(Grade g) {
        return g.value().compareTo(PASSING_SCORE) < 0;
    }

    public RegistrationStatus status(Set<Grade> grades) {
        if (grades.stream().anyMatch(g -> !g.registration().equals(this))) {
            throw new IllegalArgumentException("Argument with set of grades has a wrong registration!" +
                    " - Registration(s): [" +
                    grades.stream()
                            .map(Grade::registration)
                            .map(Record::toString)
                            .collect(Collectors.joining(",%n")) +
                    "]");
        }

        Set<Subject> gradesSubjects = grades.stream().map(Grade::subject).collect(Collectors.toSet());
        if (!gradesSubjects.containsAll(this.subjects)) {
            return RegistrationStatus.ONGOING;
        }

        if (this.endDate.isBefore(LocalDate.now())) {
            return this.getStatusAfterEventDate(grades, END_DATE);
        }

        if (this.recoveryDate.isBefore(LocalDate.now())) {
            return this.getStatusAfterEventDate(grades, RECOVERY_DATE);
        }

        return RegistrationStatus.ONGOING;

    }

    private RegistrationStatus getStatusAfterEventDate(Set<Grade> grades, String event) {
        if (hasOngoingOrPartialGrade(grades)) {
            return RegistrationStatus.ONGOING;
        }

        if (hasAnyLowGrade(grades)) {
            switch (event) {
                case END_DATE -> {
                    return RegistrationStatus.DISAPPROVED;
                }
                case RECOVERY_DATE -> {
                    return RegistrationStatus.RECOVERY;
                }
                default -> throw new IllegalArgumentException("Argument event [" +
                        event +
                        "] in getStatusAfterEventDate method is invalid!");
            }
        }

        return RegistrationStatus.APPROVED;
    }

    private boolean hasOngoingOrPartialGrade(Set<Grade> grades) {
        return grades.stream()
                .anyMatch(g -> g.type().equals(GradeType.ONGOING) || g.type().equals(GradeType.PARTIAL));
    }

    private boolean hasAnyLowGrade(Set<Grade> grades) {
        return grades.stream()
                .filter(g -> g.type().equals(GradeType.FINAL))
                .anyMatch(Registration::isLowScore);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registration that = (Registration) o;
        return Objects.equals(student, that.student) && Objects.equals(clazz, that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, clazz);
    }

    @Override
    public String toString() {
        return "Registration{" +
                "student=" + student +
                ", clazz=" + clazz +
                ", subjects=" + subjects +
                ", registrationDate=" + registrationDate +
                ", recoveryDate=" + recoveryDate +
                ", endDate=" + endDate +
                '}';
    }
}
