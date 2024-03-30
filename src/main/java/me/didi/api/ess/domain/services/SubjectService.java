package me.didi.api.ess.domain.services;

import jakarta.transaction.Transactional;
import me.didi.api.ess.domain.exceptions.EntityNotFoundException;
import me.didi.api.ess.resources.repositories.SubjectRepository;
import me.didi.api.ess.resources.repositories.entities.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository repository;

    public SubjectService(SubjectRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Subject save(Subject subject) {
        return repository.save(subject);
    }

    public List<Subject> findAll() {
        return repository.findAll();
    }

    public Subject findById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Subject with Id [" +
                        id +
                        "] Not Found!"));
    }
}
