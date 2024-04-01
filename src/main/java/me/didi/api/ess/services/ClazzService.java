package me.didi.api.ess.services;

import jakarta.transaction.Transactional;
import me.didi.api.ess.entities.Course;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.ClazzRepository;
import me.didi.api.ess.entities.Clazz;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClazzService {

    private final ClazzRepository repository;

    private final CourseService courseService;

    public ClazzService(ClazzRepository repository, CourseService courseService) {
        this.repository = repository;
        this.courseService = courseService;
    }

    @Transactional
    public Clazz save(Clazz clazz) {
        Course course = courseService.findById(clazz.getCourse().getId());
        clazz.setCourse(course);

        return repository.save(clazz);
    }

    public List<Clazz> findAll() {
        return repository.findAll();
    }

    public Clazz findById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Class with Id [" +
                        id +
                        "] Not Found!"));
    }
}
