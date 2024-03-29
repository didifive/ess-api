package me.didi.api.ess.domain;

import java.util.Objects;
import java.util.UUID;

public record Student(UUID id,
                      String givenName,
                      String familyName,
                      String guardian,
                      String photo) {

    public Student newStudent(String givenName,
                              String familyName,
                              String guardian,
                              String photo) {
        return new Student(UUID.randomUUID(),
                givenName,
                familyName,
                guardian,
                photo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(givenName, student.givenName) && Objects.equals(familyName, student.familyName) && Objects.equals(guardian, student.guardian) && Objects.equals(photo, student.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(givenName, familyName, guardian, photo);
    }
}
