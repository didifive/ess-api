package me.didi.api.ess.resources.repositories;

import me.didi.api.ess.resources.repositories.entities.ReadMessage;
import me.didi.api.ess.resources.repositories.entities.ReadMessageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadMessageRepository extends JpaRepository<ReadMessage, ReadMessageId> {
}
