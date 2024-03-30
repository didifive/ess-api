package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.Objects;

@Entity(name = "student")
public class Student extends BasicEntity implements Serializable {
    private String givenName;
    private String familyName;
    private String guardian;
    private String photo;

    public Student() {
    }

    public Student(String id,
                   String givenName,
                   String familyName,
                   String guardian,
                   String photo) {
        super(id);
        this.givenName = givenName;
        this.familyName = familyName;
        this.guardian = guardian;
        this.photo = photo;
    }

    public Student(String givenName,
                   String familyName,
                   String guardian,
                   String photo) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.guardian = guardian;
        this.photo = photo;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(getGivenName(), student.getGivenName()) && Objects.equals(getFamilyName(), student.getFamilyName()) && Objects.equals(getGuardian(), student.getGuardian()) && Objects.equals(getPhoto(), student.getPhoto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGivenName(), getFamilyName(), getGuardian(), getPhoto());
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + getId() + '\'' +
                "givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", guardian='" + guardian + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
