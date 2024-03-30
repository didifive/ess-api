package me.didi.api.ess.entities.abstracts;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@MappedSuperclass
public abstract class BasicEntity implements Serializable {

    @Id
    @UuidGenerator
    private String id;

    protected BasicEntity() {
    }

    protected BasicEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
