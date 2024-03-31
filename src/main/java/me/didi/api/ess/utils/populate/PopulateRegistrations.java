package me.didi.api.ess.utils.populate;


import me.didi.api.ess.entities.Clazz;
import me.didi.api.ess.entities.Registration;
import me.didi.api.ess.entities.Student;
import me.didi.api.ess.entities.Subject;
import me.didi.api.ess.services.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

@Component
public class PopulateRegistrations implements PopulateData {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final RegistrationService registrationService;
    private final ClazzService clazzService;
    private final StudentService studentService;
    private final SubjectService subjectService;


    public PopulateRegistrations(RegistrationService registrationService,
                                 ClazzService clazzService,
                                 StudentService studentService,
                                 SubjectService subjectService) {
        this.registrationService = registrationService;
        this.clazzService = clazzService;
        this.studentService = studentService;
        this.subjectService = subjectService;
    }

    public void populate() {
        List<Clazz> classes = clazzService.findAll();
        List<Student> students = studentService.findAll();
        List<Subject> subjects = subjectService.findAll();

        List<Registration> registrations = List.of(
                new Registration(
                        students.getFirst(),
                        classes.getFirst(),
                        new HashSet<>(subjects)
                ),
                new Registration(
                        students.get(1),
                        classes.getFirst(),
                        new HashSet<>(subjects)
                ),
                new Registration(
                        students.get(2),
                        classes.getFirst(),
                        new HashSet<>(subjects)
                ),
                new Registration(
                        students.getLast(),
                        classes.getFirst(),
                        new HashSet<>(subjects)
                )
        );

        registrations.forEach(registrationService::save);

        logger.info("Registrations saved!");
    }

}
