package me.didi.api.ess.controllers.docs;

import io.swagger.v3.oas.annotations.tags.Tag;
import me.didi.api.ess.dtos.requests.ClazzRequestDTO;
import me.didi.api.ess.dtos.responses.ClazzResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

@Tag(name = "Class Endpoints")
public interface ClazzControllerDocs {

    ResponseEntity<ClazzResponseDTO> create(
            ClazzRequestDTO dto,
            BindingResult bindingResult);

    ResponseEntity<List<ClazzResponseDTO>> findAll();

    ResponseEntity<ClazzResponseDTO> findById(String id);

}
