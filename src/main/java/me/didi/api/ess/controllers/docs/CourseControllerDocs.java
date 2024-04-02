package me.didi.api.ess.controllers.docs;

import io.swagger.v3.oas.annotations.tags.Tag;
import me.didi.api.ess.dtos.requests.CourseRequestDTO;
import me.didi.api.ess.dtos.requests.MessageRequestDTO;
import me.didi.api.ess.dtos.requests.NewsRequestDTO;
import me.didi.api.ess.dtos.requests.ShortcutRequestDTO;
import me.didi.api.ess.dtos.responses.CourseResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

@Tag(name = "Course Endpoints")
public interface CourseControllerDocs {

    ResponseEntity<CourseResponseDTO> create(
            CourseRequestDTO dto,
            BindingResult bindingResult);

    ResponseEntity<List<CourseResponseDTO>> findAll();

    ResponseEntity<CourseResponseDTO> findById(String id);

    ResponseEntity<Void> addMessage(
            String id,
            MessageRequestDTO dto,
            BindingResult bindingResult);

    ResponseEntity<Void> removeMessage(
            String id,
            String messageId);

    ResponseEntity<Void> addNews(
            String id,
            NewsRequestDTO dto,
            BindingResult bindingResult);

    ResponseEntity<Void> removeNews(
            String id,
            String newsId);

    ResponseEntity<Void> addNews(
            String id,
            ShortcutRequestDTO dto,
            BindingResult bindingResult);

    ResponseEntity<Void> removeShortcut(
            String id,
            String shortcutId);
}
