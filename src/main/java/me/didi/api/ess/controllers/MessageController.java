package me.didi.api.ess.controllers;

import me.didi.api.ess.controllers.docs.MessageControllerDocs;
import me.didi.api.ess.dtos.responses.MessageResponseDTO;
import me.didi.api.ess.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/message")
public class MessageController implements MessageControllerDocs {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<MessageResponseDTO>> findAll() {
        return ResponseEntity.ok().body(messageService.findAll().stream()
                .map(MessageResponseDTO::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(MessageResponseDTO.toDto(
                messageService.findById(id)
        ));
    }
}
