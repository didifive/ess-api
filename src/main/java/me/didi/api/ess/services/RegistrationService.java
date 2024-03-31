package me.didi.api.ess.services;

import jakarta.transaction.Transactional;
import me.didi.api.ess.entities.Clazz;
import me.didi.api.ess.entities.Registration;
import me.didi.api.ess.entities.Student;
import me.didi.api.ess.entities.Subject;
import me.didi.api.ess.entities.pks.RegistrationId;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private final RegistrationRepository repository;

    private final StudentService studentService;
    private final ClazzService clazzService;
    private final SubjectService subjectService;

    public RegistrationService(RegistrationRepository repository, StudentService studentService, ClazzService clazzService, SubjectService subjectService) {
        this.repository = repository;
        this.studentService = studentService;
        this.clazzService = clazzService;
        this.subjectService = subjectService;
    }

    @Transactional
    public Registration save(Registration registration) {
        Student student = studentService.findById(registration.getId().getStudent().getId());
        Clazz clazz = clazzService.findById(registration.getId().getClazz().getId());
        registration.getId().setStudent(student);
        registration.getId().setClazz(clazz);

        Set<Subject> subjects = registration.getSubjects().stream().map(s -> subjectService.findById(s.getId()))
                .collect(Collectors.toSet());

        registration.setSubjects(subjects);

        return repository.save(registration);
    }

    public List<Registration> findAll() {
        return repository.findAll();
    }

    public Registration findById(String studentId, String classId) {
        Student student = studentService.findById(studentId);
        Clazz clazz = clazzService.findById(classId);
        RegistrationId registrationId = new RegistrationId();
        registrationId.setStudent(student);
        registrationId.setClazz(clazz);

        return repository.findById(registrationId).orElseThrow(
                () -> new EntityNotFoundException("Registration with Id [" +
                        registrationId +
                        "] Not Found!"));
    }
}
