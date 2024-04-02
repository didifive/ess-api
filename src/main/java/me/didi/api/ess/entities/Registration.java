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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "registrations_subjects",
            joinColumns = {@JoinColumn(name = "student_id", referencedColumnName = "student_id"),
                    @JoinColumn(name = "class_id", referencedColumnName = "class_id")},
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects;
    private LocalDate registrationDate;

    @OneToMany(mappedBy = "id.registration"
            , fetch = FetchType.EAGER
            , cascade = {CascadeType.ALL}
            , orphanRemoval = true)
    private Set<Grade> grades;

    public Registration() {
    }

    public Registration(Student student,
                        Clazz clazz,
                        Set<Subject> subjects) {
        RegistrationId registrationId = new RegistrationId();
        registrationId.setStudent(student);
        registrationId.setClazz(clazz);

        this.id = registrationId;
        this.subjects = subjects;
        this.registrationDate = LocalDate.now();
    }

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

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
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

    public RegistrationStatus status() {
        if(Objects.isNull(this.grades))
            return RegistrationStatus.ONGOING;

        Set<Subject> gradesSubjects = this.grades.stream().map(g -> g.getId().getSubject()).collect(Collectors.toSet());
        if (!gradesSubjects.containsAll(this.subjects)) {
            return RegistrationStatus.ONGOING;
        }

        if (this.getId().getClazz().getEndDate().isBefore(LocalDate.now())) {
            return this.getStatusAfterEventDate(this.grades, END_DATE);
        }

        if (this.getId().getClazz().getRecoveryDate().isBefore(LocalDate.now())) {
            return this.getStatusAfterEventDate(this.grades, RECOVERY_DATE);
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
                '}';
    }
}
