package me.didi.api.ess.services;

import jakarta.transaction.Transactional;
import me.didi.api.ess.entities.News;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    private final NewsRepository repository;

    public NewsService(NewsRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public News save(News news) {
        return repository.save(news);
    }

    public List<News> findAll() {
        return repository.findAll();
    }

    public News findById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("News with Id [" +
                        id +
                        "] Not Found!"));
    }
}
