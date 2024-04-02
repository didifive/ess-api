package me.didi.api.ess.controllers;

import me.didi.api.ess.controllers.docs.NewsControllerDocs;
import me.didi.api.ess.dtos.responses.NewsResponseDTO;
import me.didi.api.ess.services.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/news")
public class NewsController implements NewsControllerDocs {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<List<NewsResponseDTO>> findAll() {
        return ResponseEntity.ok().body(newsService.findAll().stream()
                .map(NewsResponseDTO::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponseDTO> findById(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(NewsResponseDTO.toDto(
                newsService.findById(id)
        ));
    }
}
