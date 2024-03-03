package me.didi.api.ess.resources.repositories;

import me.didi.api.ess.resources.repositories.entities.Grade;
import me.didi.api.ess.resources.repositories.entities.GradeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, GradeId> {
}
