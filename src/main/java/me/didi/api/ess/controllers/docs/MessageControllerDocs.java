package me.didi.api.ess.controllers.docs;

import io.swagger.v3.oas.annotations.tags.Tag;
import me.didi.api.ess.dtos.responses.MessageResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Message Endpoints")
public interface MessageControllerDocs {

    ResponseEntity<List<MessageResponseDTO>> findAll();

    ResponseEntity<MessageResponseDTO> findById(String id);
}
