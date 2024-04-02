package me.didi.api.ess.controllers.docs;

import io.swagger.v3.oas.annotations.tags.Tag;
import me.didi.api.ess.dtos.requests.SubjectRequestDTO;
import me.didi.api.ess.dtos.responses.SubjectResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

@Tag(name = "Subject Endpoints")
public interface SubjectControllerDocs {

    ResponseEntity<SubjectResponseDTO> create(
            SubjectRequestDTO dto,
            BindingResult bindingResult);

    ResponseEntity<List<SubjectResponseDTO>> findAll();

    ResponseEntity<SubjectResponseDTO> findById(String id);

}
