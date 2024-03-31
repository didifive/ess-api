package me.didi.api.ess.utils.populate;


import me.didi.api.ess.services.ClazzService;
import me.didi.api.ess.services.CourseService;
import me.didi.api.ess.entities.Clazz;
import me.didi.api.ess.entities.Course;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
                        courses.getFirst(),
                        LocalDate.ofYearDay(2024,10),
                        LocalDate.ofYearDay(2024,310),
                        LocalDate.ofYearDay(2024,350)),
                new Clazz("Ano 2 - A",
                        courses.getFirst(),
                        LocalDate.ofYearDay(2024,10),
                        LocalDate.ofYearDay(2024,310),
                        LocalDate.ofYearDay(2024,350)),
                new Clazz("Ano 1 - B",
                        courses.getFirst(),
                        LocalDate.ofYearDay(2024,10),
                        LocalDate.ofYearDay(2024,310),
                        LocalDate.ofYearDay(2024,350)),
                new Clazz("Ano 2 - B",
                        courses.getFirst(),
                        LocalDate.ofYearDay(2024,10),
                        LocalDate.ofYearDay(2024,310),
                        LocalDate.ofYearDay(2024,350)),
                new Clazz("Mensal 1",
                        courses.getLast(),
                        LocalDate.of(2024, 6,1),
                        LocalDate.of(2024, 6,27),
                        LocalDate.of(2024, 7,1)));

        classes.forEach(service::save);

        logger.info("Classes saved!");
    }

}
