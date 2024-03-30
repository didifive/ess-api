package me.didi.api.ess.domain.services;

import me.didi.api.ess.resources.repositories.StudentRepository;
import me.didi.api.ess.resources.repositories.entities.Student;
import me.didi.api.ess.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }


    public Student save(Student student) {
        return repository.save(student);
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Student findById(String id) {
        UUID studentId = UUID.fromString(id);
        return repository.findById(studentId).orElseThrow(
                () -> new EntityNotFoundException("Student with Id [" +
                        id +
                        "] Not Found!"));
    }
}
