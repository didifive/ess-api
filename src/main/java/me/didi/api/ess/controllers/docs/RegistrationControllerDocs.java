package me.didi.api.ess.controllers.docs;

import io.swagger.v3.oas.annotations.tags.Tag;
import me.didi.api.ess.dtos.requests.RegistrationRequestDTO;
import me.didi.api.ess.dtos.responses.RegistrationResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

@Tag(name = "Registration Endpoints")
public interface RegistrationControllerDocs {

    ResponseEntity<RegistrationResponseDTO> create(
            RegistrationRequestDTO dto,
            BindingResult bindingResult);

    ResponseEntity<List<RegistrationResponseDTO>> findAll();

    ResponseEntity<RegistrationResponseDTO> findById(
            String studentId,
            String classId);

}
