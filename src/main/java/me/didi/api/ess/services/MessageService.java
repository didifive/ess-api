package me.didi.api.ess.services;

import jakarta.transaction.Transactional;
import me.didi.api.ess.entities.Message;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public List<Message> findAll() {
        return repository.findAll();
    }

    public Message findById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Message with Id [" +
                        id +
                        "] Not Found!"));
    }
}
