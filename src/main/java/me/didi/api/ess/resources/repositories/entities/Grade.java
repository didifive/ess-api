package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity(name="grade")
public class Grade {
    @EmbeddedId
    private GradeId id;
    @Column(name="grade_value", precision = 5, scale = 2)
    private BigDecimal value;

    public GradeId getId() {
        return id;
    }

    public void setId(GradeId id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal gradeValue) {
        this.value = gradeValue;
    }
}
