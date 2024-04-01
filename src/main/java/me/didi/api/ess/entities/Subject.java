package me.didi.api.ess.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import me.didi.api.ess.entities.abstracts.BasicItem;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity(name = "subject")
public class Subject extends BasicItem implements Serializable {
    @OneToMany(mappedBy = "id.subject"
            , fetch = FetchType.LAZY
            , cascade = {CascadeType.ALL}
            , orphanRemoval = true)
    private Set<Grade> grades;

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }
    public Subject() {
    }

    public Subject(String id,
                      String icon,
                      String title,
                      String description) {
        super(id, icon, title, description);
    }

    public Subject(String icon,
                      String title,
                      String description) {
        super(icon, title, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subject subject = (Subject) o;
        return Objects.equals(getIcon(), subject.getIcon()) 
                && Objects.equals(getTitle(), subject.getTitle())
                && Objects.equals(getDescription(), subject.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIcon(), getTitle(), getDescription());
    }
}
