package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.Embeddable;
import me.didi.api.ess.domain.enums.GradeType;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReadMessageId implements Serializable {

    private String studentId;
    private String messageId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadMessageId that = (ReadMessageId) o;
        return Objects.equals(getStudentId(), that.getStudentId()) && Objects.equals(getMessageId(), that.getMessageId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStudentId(), getMessageId());
    }
}
