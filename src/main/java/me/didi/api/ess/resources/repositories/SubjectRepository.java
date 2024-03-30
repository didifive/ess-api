package me.didi.api.ess.resources.repositories;

import me.didi.api.ess.resources.repositories.entities.Shortcut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Shortcut, String> {
}
