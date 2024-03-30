package me.didi.api.ess.utils;


import me.didi.api.ess.services.ClazzService;
import me.didi.api.ess.services.CourseService;
import me.didi.api.ess.entities.Clazz;
import me.didi.api.ess.entities.Course;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class PopulateClasses implements PopulateData {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final ClazzService service;
    private final CourseService courseService;

    public PopulateClasses(ClazzService service, CourseService courseService) {
        this.service = service;
        this.courseService = courseService;
    }

    public void populate() {
        List<Course> courses = courseService.findAll();

        List<Clazz> classes = List.of(
                new Clazz("Ano 1 - A",
                        courses.getFirst()),
                new Clazz("Ano 2 - A",
                        courses.getFirst()),
                new Clazz("Ano 1 - B",
                        courses.getFirst()),
                new Clazz("Ano 2 - B",
                        courses.getFirst()),
                new Clazz("Mensal 1",
                        courses.getLast()));

        classes.forEach(service::save);

        logger.info("Classes saved!");
    }

}
