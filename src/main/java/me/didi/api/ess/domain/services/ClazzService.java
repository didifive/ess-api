package me.didi.api.ess.domain.services;

import jakarta.transaction.Transactional;
import me.didi.api.ess.domain.exceptions.EntityNotFoundException;
import me.didi.api.ess.resources.repositories.ClazzRepository;
import me.didi.api.ess.resources.repositories.entities.Clazz;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClazzService {

    private final ClazzRepository repository;

    public ClazzService(ClazzRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Clazz save(Clazz clazz) {
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
