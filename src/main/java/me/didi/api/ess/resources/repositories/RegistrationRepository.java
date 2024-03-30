package me.didi.api.ess.resources.repositories;

import me.didi.api.ess.resources.repositories.entities.Registration;
import me.didi.api.ess.resources.repositories.entities.RegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, RegistrationId> {
}
