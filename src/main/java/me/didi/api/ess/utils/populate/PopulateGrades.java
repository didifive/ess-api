package me.didi.api.ess.utils.populate;


import me.didi.api.ess.entities.Grade;
import me.didi.api.ess.entities.Registration;
import me.didi.api.ess.entities.Subject;
import me.didi.api.ess.enums.GradeType;
import me.didi.api.ess.services.GradeService;
import me.didi.api.ess.services.RegistrationService;
import me.didi.api.ess.services.SubjectService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

@Component
public class PopulateGrades implements PopulateData {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final GradeService gradeService;
    private final RegistrationService registrationService;
    private final SubjectService subjectService;


    public PopulateGrades(RegistrationService registrationService,
                          GradeService gradeService,
                          SubjectService subjectService) {
        this.registrationService = registrationService;
        this.gradeService = gradeService;
        this.subjectService = subjectService;
    }

    public void populate() {
        List<Registration> registrations = registrationService.findAll();
        List<Subject> subjects = subjectService.findAll();

        Registration registration = registrations.getFirst();

        List<Grade> grades = List.of(
                new Grade(
                        registration,
                        subjects.getFirst(),
                        GradeType.FINAL,
                        BigDecimal.valueOf(7.1d)
                ),
                new Grade(
                        registration,
                        subjects.get(1),
                        GradeType.PARTIAL,
                        BigDecimal.valueOf(10d)
                ), new Grade(
                        registration,
                        subjects.get(2),
                        GradeType.PARTIAL,
                        BigDecimal.valueOf(10d)
                ), new Grade(
                        registration,
                        subjects.get(3),
                        GradeType.PARTIAL,
                        BigDecimal.valueOf(5d)
                ), new Grade(
                        registration,
                        subjects.getLast(),
                        GradeType.ONGOING,
                        BigDecimal.valueOf(0)
                ));

        grades.forEach(gradeService::save);

        logger.info("Grades saved!");
    }

}
