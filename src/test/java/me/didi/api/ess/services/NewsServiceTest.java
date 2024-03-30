package me.didi.api.ess.services;

import me.didi.api.ess.entities.News;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.NewsRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opentest4j.MultipleFailuresError;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static me.didi.api.ess.utils.Assertions.assertThrowsExceptionWithCorrectMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("News Service Tests")
@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsRepository repository;
    @InjectMocks
    private NewsService service;

    private News news;

    private void assertNews(News expected, News result) throws MultipleFailuresError {
        assertAll(
                "Assert that Expected News has been returned"
                , () -> assertNotNull(result)
                , () -> assertEquals(expected.getId(), result.getId())
                , () -> assertEquals(expected.getIcon(), result.getIcon())
                , () -> assertEquals(expected.getTitle(), result.getTitle())
                , () -> assertEquals(expected.getDescription(), result.getDescription())
        );
    }

    @BeforeEach
    void setup() {
        news = Instancio.create(News.class);
    }

    @Test
    @DisplayName("1. Save New News")
    void saveNewNews() {
        when(repository.save(any(News.class))).thenReturn(news);

        News result = service.save(news);

        assertNews(news, result);
    }
    
    @Test
    @DisplayName("2. List News")
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(news));

        List<News> result = service.findAll();

        assertAll("Assert that a list of the Newses has been returned",
                () -> assertInstanceOf(List.class, result),
                () -> assertEquals(1, result.size()),
                () -> assertNews(news, result.getFirst())
        );

    }

    @Test
    @DisplayName("3. Find News by Id")
    void findById() {
        News news = Instancio.create(News.class);
        String id = news.getId();

        when(repository.findById(any(String.class))).thenReturn(Optional.of(news));

        News result = service.findById(id);

        assertNews(news, result);

    }

    @Test
    @DisplayName("3.1. Throw Exception when try find News with Invalid Id")
    void throwsExceptionWhenTryFindByInvalidId() {
        String invalidId = UUID.randomUUID().toString();

        when(repository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrowsExceptionWithCorrectMessage(
                () -> service.findById(invalidId),
                EntityNotFoundException.class,
                "News with Id [" +
                        invalidId +
                        "] Not Found!"
        );

    }


}