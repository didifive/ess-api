package me.didi.api.ess.controllers;

import jakarta.validation.Valid;
import me.didi.api.ess.controllers.docs.CourseControllerDocs;
import me.didi.api.ess.dtos.requests.CourseRequestDTO;
import me.didi.api.ess.dtos.requests.MessageRequestDTO;
import me.didi.api.ess.dtos.requests.NewsRequestDTO;
import me.didi.api.ess.dtos.requests.ShortcutRequestDTO;
import me.didi.api.ess.dtos.responses.CourseResponseDTO;
import me.didi.api.ess.services.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static me.didi.api.ess.utils.VerifyError.verifyRequestBodyError;

@RestController
@RequestMapping("api/v1/course")
public class CourseController implements CourseControllerDocs {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(
            @RequestBody @Valid CourseRequestDTO dto,
            BindingResult bindingResult) {

        verifyRequestBodyError(bindingResult);

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

    @PostMapping("/{id}/message/add")
    public ResponseEntity<Void> addMessage(
            @PathVariable String id,
            @RequestBody @Valid MessageRequestDTO dto,
            BindingResult bindingResult) {

        verifyRequestBodyError(bindingResult);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/v1/message/{id}")
                .buildAndExpand(courseService.addMessage(id, MessageRequestDTO.toEntity(dto))).toUri();

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}/message/{messageId}")
    public ResponseEntity<Void> removeMessage(
            @PathVariable String id,
            @PathVariable String messageId) {

        courseService.removeMessage(id, messageId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/news/add")
    public ResponseEntity<Void> addNews(
            @PathVariable String id,
            @RequestBody @Valid NewsRequestDTO dto,
            BindingResult bindingResult) {

        verifyRequestBodyError(bindingResult);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/v1/news/{id}")
                .buildAndExpand(courseService.addNews(id, NewsRequestDTO.toEntity(dto))).toUri();

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}/news/{newsId}")
    public ResponseEntity<Void> removeNews(
            @PathVariable String id,
            @PathVariable String newsId) {

        courseService.removeNews(id, newsId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/shortcut/add")
    public ResponseEntity<Void> addNews(
            @PathVariable String id,
            @RequestBody @Valid ShortcutRequestDTO dto,
            BindingResult bindingResult) {

        verifyRequestBodyError(bindingResult);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/v1/shortcut/{id}")
                .buildAndExpand(courseService.addShortcut(id, ShortcutRequestDTO.toEntity(dto))).toUri();

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}/shortcut/{shortcutId}")
    public ResponseEntity<Void> removeShortcut(
            @PathVariable String id,
            @PathVariable String shortcutId) {

        courseService.removeShortcut(id, shortcutId);

        return ResponseEntity.noContent().build();
    }
}
