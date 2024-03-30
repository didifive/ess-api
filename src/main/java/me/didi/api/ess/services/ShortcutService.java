package me.didi.api.ess.services;

import jakarta.transaction.Transactional;
import me.didi.api.ess.entities.Shortcut;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.ShortcutRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortcutService {

    private final ShortcutRepository repository;

    public ShortcutService(ShortcutRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Shortcut save(Shortcut shortcut) {
        return repository.save(shortcut);
    }

    public List<Shortcut> findAll() {
        return repository.findAll();
    }

    public Shortcut findById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Shortcut with Id [" +
                        id +
                        "] Not Found!"));
    }
}
