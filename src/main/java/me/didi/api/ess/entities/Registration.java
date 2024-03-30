package me.didi.api.ess.entities;

import jakarta.persistence.*;
import me.didi.api.ess.entities.pks.RegistrationId;
import me.didi.api.ess.enums.GradeType;
import me.didi.api.ess.enums.RegistrationStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "registration")
public class Registration implements Serializable {
    public static final String END_DATE = "endDate";
    public static final String RECOVERY_DATE = "recoveryDate";
    public static final BigDecimal PASSING_SCORE = new BigDecimal("7.00");
    @EmbeddedId
    private RegistrationId id;
    @ManyToMany
    @JoinTable(
            name = "registrations_subjects",
            joinColumns = {@JoinColumn(name = "student_id", referencedColumnName = "student_id"),
                    @JoinColumn(name = "class_id", referencedColumnName = "class_id")},
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects;
    private LocalDate registrationDate;
    private LocalDate recoveryDate;
    private LocalDate endDate;

    public RegistrationId getId() {
        return id;
    }

    public void setId(RegistrationId id) {
        this.id = id;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getRecoveryDate() {
        return recoveryDate;
    }

    public void setRecoveryDate(LocalDate recoveryDate) {
        this.recoveryDate = recoveryDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registration that = (Registration) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public RegistrationStatus status(Set<Grade> grades) {
        if (grades.stream().anyMatch(g -> !g.getId().getRegistration().equals(this))) {
            throw new IllegalArgumentException("Argument with set of grades has a wrong registration!" +
                    " - Registration(s): [" +
                    grades.stream()
                            .map(g -> g.getId().getRegistration())
                            .map(Registration::toString)
                            .collect(Collectors.joining(",%n")) +
                    "]");
        }

        Set<Subject> gradesSubjects = grades.stream().map(g -> g.getId().getSubject()).collect(Collectors.toSet());
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
                .anyMatch(g -> g.getGradeType().equals(GradeType.ONGOING) || g.getGradeType().equals(GradeType.PARTIAL));
    }

    private boolean hasAnyLowGrade(Set<Grade> grades) {
        return grades.stream()
                .filter(g -> g.getGradeType().equals(GradeType.FINAL))
                .anyMatch(g -> g.getValue().compareTo(PASSING_SCORE) < 0);
    }

    @Override
    public String toString() {
        return "Registration{" +
                "id=" + id +
                ", subjects=" + subjects +
                ", registrationDate=" + registrationDate +
                ", recoveryDate=" + recoveryDate +
                ", endDate=" + endDate +
                '}';
    }
}
