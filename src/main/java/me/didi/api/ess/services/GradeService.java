package me.didi.api.ess.services;

import jakarta.transaction.Transactional;
import me.didi.api.ess.entities.Grade;
import me.didi.api.ess.entities.Registration;
import me.didi.api.ess.entities.Subject;
import me.didi.api.ess.entities.pks.GradeId;
import me.didi.api.ess.enums.GradeType;
import me.didi.api.ess.exceptions.DataIntegrityViolationException;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.exceptions.EssException;
import me.didi.api.ess.repositories.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class GradeService {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final GradeRepository repository;

    private final RegistrationService registrationService;
    private final SubjectService subjectService;

    public GradeService(GradeRepository repository,
                        RegistrationService registrationService,
                        SubjectService subjectService) {
        this.repository = repository;
        this.registrationService = registrationService;
        this.subjectService = subjectService;
    }

    @Transactional
    public Grade save(Grade grade) {
        String studentId = grade.getId().getRegistration().getId().getStudent().getId();
        String classId = grade.getId().getRegistration().getId().getClazz().getId();
        Registration registration = registrationService.findById(studentId, classId);
        Subject subject = subjectService.findById(grade.getId().getSubject().getId());

        if (!registration.getSubjects().contains(subject)) {
            throw new DataIntegrityViolationException("The subject [" +
                    subject.getId() +
                    "] is not within the scope of registration");
        }

        grade.getId().setRegistration(registration);
        grade.getId().setSubject(subject);

        try {
            Grade existingGrade = this.findById(grade.getId().getRegistration().getId().getStudent().getId(),
                    grade.getId().getRegistration().getId().getClazz().getId(),
                    grade.getId().getSubject().getId());
            if (existingGrade.getGradeType().equals(GradeType.FINAL)
                    && !grade.getGradeType().equals(GradeType.FINAL)) {
                throw new DataIntegrityViolationException("There is already a type of final grade informed");
            }
            if (existingGrade.getGradeType().equals(GradeType.PARTIAL)
                    && grade.getGradeType().equals(GradeType.ONGOING)) {
                throw new DataIntegrityViolationException("There is already a type of partial grade informed");
            }
        } catch (EntityNotFoundException e) {
            logger.info("There is no grade for registration and discipline, so there is no grade conflict.");
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        } catch (Exception e) {
            throw new EssException("Erro desconhecido :o");
        }

        return repository.save(grade);
    }

    public List<Grade> findAll() {
        return repository.findAll();
    }

    public Grade findById(String studentId, String classId, String subjectId) {
        Registration registration = registrationService.findById(studentId, classId);
        Subject subject = subjectService.findById(subjectId);
        GradeId gradeId = new GradeId();
        gradeId.setRegistration(registration);
        gradeId.setSubject(subject);

        return repository.findById(gradeId).orElseThrow(
                () -> new EntityNotFoundException("Grade with Id [" +
                        gradeId +
                        "] Not Found!"));
    }
}
