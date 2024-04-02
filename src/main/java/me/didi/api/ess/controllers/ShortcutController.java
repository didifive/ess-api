package me.didi.api.ess.controllers;

import me.didi.api.ess.controllers.docs.ShortcutControllerDocs;
import me.didi.api.ess.dtos.responses.ShortcutResponseDTO;
import me.didi.api.ess.services.ShortcutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/shortcut")
public class ShortcutController implements ShortcutControllerDocs {

    private final ShortcutService shortcutService;

    public ShortcutController(ShortcutService shortcutService) {
        this.shortcutService = shortcutService;
    }

    @GetMapping
    public ResponseEntity<List<ShortcutResponseDTO>> findAll() {
        return ResponseEntity.ok().body(shortcutService.findAll().stream()
                .map(ShortcutResponseDTO::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShortcutResponseDTO> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(ShortcutResponseDTO.toDto(
                shortcutService.findById(id)
        ));
    }
}
