package me.didi.api.ess.controllers;

import jakarta.validation.Valid;
import me.didi.api.ess.dtos.requests.StudentRequestDTO;
import me.didi.api.ess.dtos.responses.StudentResponseDTO;
import me.didi.api.ess.exceptions.BadRequestBodyException;
import me.didi.api.ess.services.StudentService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> create(
            @RequestBody @Valid StudentRequestDTO dto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new BadRequestBodyException(
                    bindingResult.getFieldErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.joining("||")));
        }

        StudentResponseDTO savedStudent =
                StudentResponseDTO.toDto(studentService.save(StudentRequestDTO.toEntity(dto)));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedStudent.id()).toUri();

        return ResponseEntity.created(uri).body(savedStudent);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> findAll() {
        return ResponseEntity.ok().body(studentService.findAll().stream()
                .map(StudentResponseDTO::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(StudentResponseDTO.toDto(
                studentService.findById(id)
        ));
    }
}
