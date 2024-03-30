package me.didi.api.ess.repositories;

import me.didi.api.ess.entities.Registration;
import me.didi.api.ess.entities.pks.RegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, RegistrationId> {
}
