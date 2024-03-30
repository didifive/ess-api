package me.didi.api.ess.repositories;

import me.didi.api.ess.entities.Grade;
import me.didi.api.ess.entities.pks.GradeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, GradeId> {
}
