package me.didi.api.ess.services;

import jakarta.transaction.Transactional;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.CourseRepository;
import me.didi.api.ess.entities.Course;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public Course save(Course course) {
        return repository.save(course);
    }

    public List<Course> findAll() {
        return repository.findAll();
    }

    public Course findById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Course with Id [" +
                        id +
                        "] Not Found!"));
    }
}
