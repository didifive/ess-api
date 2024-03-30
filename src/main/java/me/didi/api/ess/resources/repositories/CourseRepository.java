package me.didi.api.ess.resources.repositories;

import me.didi.api.ess.resources.repositories.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
}
