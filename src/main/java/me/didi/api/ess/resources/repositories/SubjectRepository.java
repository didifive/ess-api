package me.didi.api.ess.resources.repositories;

import me.didi.api.ess.resources.repositories.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {
}
