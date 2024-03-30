package me.didi.api.ess.repositories;

import me.didi.api.ess.entities.Shortcut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortcutRepository extends JpaRepository<Shortcut, String> {
}
