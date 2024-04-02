package me.didi.api.ess.controllers.docs;

import io.swagger.v3.oas.annotations.tags.Tag;
import me.didi.api.ess.dtos.responses.NewsResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "News Endpoints")
public interface NewsControllerDocs {

    ResponseEntity<List<NewsResponseDTO>> findAll();

    ResponseEntity<NewsResponseDTO> findById(String id);
}
