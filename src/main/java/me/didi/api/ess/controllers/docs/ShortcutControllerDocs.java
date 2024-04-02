package me.didi.api.ess.controllers.docs;

import io.swagger.v3.oas.annotations.tags.Tag;
import me.didi.api.ess.dtos.responses.ShortcutResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Shortcut Endpoints")
public interface ShortcutControllerDocs {

    ResponseEntity<List<ShortcutResponseDTO>> findAll();

    ResponseEntity<ShortcutResponseDTO> findById(String id);
}
