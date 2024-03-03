package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity(name="grade")
public class Grade {
    @EmbeddedId
    private GradeId id;
    @Column(scale = 4, precision = 2)
    private BigDecimal gradeValue;

    public GradeId getId() {
        return id;
    }

    public void setId(GradeId id) {
        this.id = id;
    }

    public BigDecimal getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(BigDecimal gradeValue) {
        this.gradeValue = gradeValue;
    }
}
