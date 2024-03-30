package me.didi.api.ess.resources.repositories;

import me.didi.api.ess.resources.repositories.entities.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClazzRepository extends JpaRepository<Clazz, UUID> {
}
