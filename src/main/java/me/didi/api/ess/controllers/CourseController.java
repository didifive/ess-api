package me.didi.api.ess.controllers;

import jakarta.validation.Valid;
import me.didi.api.ess.dtos.requests.CourseRequestDTO;
import me.didi.api.ess.dtos.responses.CourseResponseDTO;
import me.didi.api.ess.exceptions.BadRequestBodyException;
import me.didi.api.ess.services.CourseService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(
            @RequestBody @Valid CourseRequestDTO dto,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new BadRequestBodyException(
                    bindingResult.getFieldErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.joining("||")));
        }

        CourseResponseDTO savedCourse =
                CourseResponseDTO.toDto(courseService.save(CourseRequestDTO.toEntity(dto)));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCourse.id()).toUri();

        return ResponseEntity.created(uri).body(savedCourse);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> findAll() {
        return ResponseEntity.ok().body(courseService.findAll().stream()
                .map(CourseResponseDTO::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(CourseResponseDTO.toDto(
                courseService.findById(id)
        ));
    }
}
