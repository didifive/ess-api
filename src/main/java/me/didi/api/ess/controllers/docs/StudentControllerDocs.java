package me.didi.api.ess.controllers.docs;

import io.swagger.v3.oas.annotations.tags.Tag;
import me.didi.api.ess.dtos.requests.StudentRequestDTO;
import me.didi.api.ess.dtos.responses.StudentResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

@Tag(name = "Student Endpoints")
public interface StudentControllerDocs {

    ResponseEntity<StudentResponseDTO> create(
            StudentRequestDTO dto,
            BindingResult bindingResult);

    ResponseEntity<List<StudentResponseDTO>> findAll();

    ResponseEntity<StudentResponseDTO> findById(String id);

}
