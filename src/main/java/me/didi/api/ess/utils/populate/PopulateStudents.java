package me.didi.api.ess.utils.populate;


import me.didi.api.ess.services.StudentService;
import me.didi.api.ess.entities.Student;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class PopulateStudents implements PopulateData {
    public static final String LINK_PHOTO = "linkPhoto";
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final StudentService service;

    public PopulateStudents(StudentService service) {
        this.service = service;
    }

    public void populate() {
        List<Student> students = List.of(
                new Student(
                        "Joana",
                        "Silva",
                        "Pai",
                        LINK_PHOTO
                ),
                new Student(
                        "Maria",
                        "Souza",
                        "Mãe",
                        LINK_PHOTO
                ),
                new Student(
                        "Ana",
                        "Pinho",
                        "Tia",
                        LINK_PHOTO
                ),
                new Student(
                        "João",
                        "Oliveira",
                        "Mãe",
                        LINK_PHOTO
                )
        );

        students.forEach(service::save);

        logger.info("Students saved!");
    }

}
