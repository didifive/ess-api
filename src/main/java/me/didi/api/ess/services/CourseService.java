package me.didi.api.ess.services;

import jakarta.transaction.Transactional;
import me.didi.api.ess.entities.Course;
import me.didi.api.ess.entities.Message;
import me.didi.api.ess.entities.News;
import me.didi.api.ess.entities.Shortcut;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class CourseService {

    private final CourseRepository repository;
    private final MessageService messageService;
    private final NewsService newsService;
    private final ShortcutService shortcutService;

    public CourseService(CourseRepository repository, MessageService messageService, NewsService newsService, ShortcutService shortcutService) {
        this.repository = repository;
        this.messageService = messageService;
        this.newsService = newsService;
        this.shortcutService = shortcutService;
    }


    @Transactional
    public Course save(Course course) {
        return repository.save(course);
    }

    public List<Course> findAll() {
        return repository.findAll();
    }

    public Course findById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Course with Id [" +
                        id +
                        "] Not Found!"));
    }

    public String addMessage(String courseId, Message message) {
        Course course = this.findById(courseId);

        Set<Message> messages = Objects.isNull(course.getMessages())
                ? new HashSet<>()
                : course.getMessages();

        Message newMessage = messageService.save(message);

        messages.add(newMessage);

        course.setMessages(messages);

        this.save(course);

        return newMessage.getId();
    }

    public void removeMessage(String courseId, String messageId) {
        Course course = this.findById(courseId);

        Message message = messageService.findById(messageId);

        Set<Message> messages = Objects.isNull(course.getMessages())
                ? new HashSet<>()
                : course.getMessages();

        messages.remove(message);

        course.setMessages(messages);

        this.save(course);
    }

    public String addNews(String courseId, News news) {
        Course course = this.findById(courseId);

        Set<News> newsSet = Objects.isNull(course.getNews())
                ? new HashSet<>()
                : course.getNews();

        News newNews = newsService.save(news);

        newsSet.add(newNews);

        course.setNews(newsSet);

        this.save(course);

        return newNews.getId();
    }

    public void removeNews(String courseId, String shortcutId) {
        Course course = this.findById(courseId);

        News news = newsService.findById(shortcutId);

        Set<News> shortcuts = Objects.isNull(course.getNews())
                ? new HashSet<>()
                : course.getNews();

        shortcuts.remove(news);

        course.setNews(shortcuts);

        repository.save(course);
    }

    public String addShortcut(String courseId, Shortcut shortcut) {
        Course course = this.findById(courseId);

        Set<Shortcut> shortcuts = Objects.isNull(course.getShortcuts())
                ? new HashSet<>()
                : course.getShortcuts();

        Shortcut newShortcut = shortcutService.save(shortcut);

        shortcuts.add(newShortcut);

        course.setShortcuts(shortcuts);

        repository.save(course);

        return newShortcut.getId();
    }

    public void removeShortcut(String courseId, String shortcutId) {
        Course course = this.findById(courseId);

        Shortcut shortcut = shortcutService.findById(shortcutId);

        Set<Shortcut> shortcuts = Objects.isNull(course.getShortcuts())
                ? new HashSet<>()
                : course.getShortcuts();

        shortcuts.remove(shortcut);

        course.setShortcuts(shortcuts);

        repository.save(course);
    }
}
