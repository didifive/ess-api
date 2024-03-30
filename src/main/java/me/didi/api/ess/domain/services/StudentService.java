package me.didi.api.ess.domain.services;

import jakarta.transaction.Transactional;
import me.didi.api.ess.domain.exceptions.EntityNotFoundException;
import me.didi.api.ess.resources.repositories.StudentRepository;
import me.didi.api.ess.resources.repositories.entities.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public Student save(Student student) {
        return repository.save(student);
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Student findById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Student with Id [" +
                        id +
                        "] Not Found!"));
    }
}
