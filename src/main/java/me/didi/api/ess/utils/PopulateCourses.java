package me.didi.api.ess.utils;


import me.didi.api.ess.domain.enums.Frequency;
import me.didi.api.ess.domain.services.CourseService;
import me.didi.api.ess.resources.repositories.entities.Course;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class PopulateCourses implements PopulateData {
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final CourseService service;

    public PopulateCourses(CourseService service) {
        this.service = service;
    }

    public void populate() {
        List<Course> courses = List.of(
                new Course("Curso 1",
                        Frequency.YEARLY),
                new Course("Curso 2",
                        Frequency.SEMIANNUALLY),
                new Course("Curso 3",
                        Frequency.MONTHLY));

        courses.forEach(service::save);

        logger.info("Courses saved!");
    }

}
