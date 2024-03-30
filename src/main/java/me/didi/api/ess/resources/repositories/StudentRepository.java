package me.didi.api.ess.resources.repositories;

import me.didi.api.ess.resources.repositories.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
}
