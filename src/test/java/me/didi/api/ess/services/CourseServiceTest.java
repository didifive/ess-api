package me.didi.api.ess.services;

import me.didi.api.ess.entities.Course;
import me.didi.api.ess.entities.Message;
import me.didi.api.ess.entities.News;
import me.didi.api.ess.entities.Shortcut;
import me.didi.api.ess.exceptions.EntityNotFoundException;
import me.didi.api.ess.repositories.CourseRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opentest4j.MultipleFailuresError;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static me.didi.api.ess.utils.Assertions.assertThrowsExceptionWithCorrectMessage;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Course Service Tests")
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository repository;
    @Mock
    private MessageService messageService;
    @Mock
    private NewsService newsService;
    @Mock
    private ShortcutService shortcutService;
    @InjectMocks
    private CourseService courseService;

    private Course course;

    private ArgumentCaptor<Course> courseArgumentCaptor;

    private static Stream<Arguments> providedCourses() {
        Course course1 = Instancio.create(Course.class);
        Course course2 = Instancio.of(Course.class)
                .set(field("messages"), null)
                .set(field("news"), null)
                .set(field("shortcuts"), null)
                .create();

        return Stream.of(
                Arguments.of(course1),
                Arguments.of(course2)
        );
    }

    private void assertCourse(Course expected, Course result) throws MultipleFailuresError {
        assertAll(
                "Assert that Expected Course has been returned"
                , () -> assertNotNull(result)
                , () -> assertEquals(expected.getId(), result.getId())
                , () -> assertEquals(expected.getName(), result.getName())
        );
    }

    @BeforeEach
    void setup() {
        course = Instancio.create(Course.class);
        courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
    }

    @Test
    @DisplayName("1. Save New Course")
    void saveNewCourse() {
        when(repository.save(any(Course.class))).thenReturn(course);

        Course result = courseService.save(course);

        assertCourse(course, result);
    }

    @Test
    @DisplayName("2. List Course")
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(course));

        List<Course> result = courseService.findAll();

        assertAll("Assert that a list of the Courses has been returned",
                () -> assertInstanceOf(List.class, result),
                () -> assertEquals(1, result.size()),
                () -> assertCourse(course, result.getFirst())
        );

    }

    @Test
    @DisplayName("3. Find Course by Id")
    void findById() {
        String id = course.getId();

        when(repository.findById(any(String.class))).thenReturn(Optional.of(course));

        Course result = courseService.findById(id);

        assertCourse(course, result);

    }

    @Test
    @DisplayName("3.1. Throw Exception when try find Course with Invalid Id")
    void throwsExceptionWhenTryFindByInvalidId() {
        String invalidId = UUID.randomUUID().toString();

        when(repository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrowsExceptionWithCorrectMessage(
                () -> courseService.findById(invalidId),
                EntityNotFoundException.class,
                "Course with Id [" +
                        invalidId +
                        "] Not Found!"
        );

    }

    @ParameterizedTest
    @MethodSource("providedCourses")
    @DisplayName("4. Add Message to the Course")
    void addMessageToCourse(Course course) {
        Message message = Instancio.create(Message.class);

        when(repository.findById(course.getId())).thenReturn(Optional.of(course));

        courseService.addMessage(course.getId(), message);

        verify(repository).save(courseArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);

        assertTrue(courseArgumentCaptor.getValue().getMessages().contains(message));
    }

    @ParameterizedTest
    @MethodSource("providedCourses")
    @DisplayName("5. Remove Message to the Course")
    void removeMessageToCourse(Course course) {
        Message messageToRemove = Objects.isNull(course.getMessages())
                ? Instancio.create(Message.class)
                : course.getMessages().stream()
                            .findFirst().orElse(new Message());

        when(repository.findById(course.getId())).thenReturn(Optional.of(course));
        when(messageService.findById(messageToRemove.getId())).thenReturn(messageToRemove);

        courseService.removeMessage(course.getId(), messageToRemove.getId());

        verify(messageService).findById(messageToRemove.getId());
        verifyNoMoreInteractions(messageService);
        verify(repository).save(courseArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);

        assertFalse(courseArgumentCaptor.getValue().getMessages().contains(messageToRemove));
    }

    @ParameterizedTest
    @MethodSource("providedCourses")
    @DisplayName("6. Add News to the Course")
    void addNewsToCourse(Course course) {
        News news = Instancio.create(News.class);

        when(repository.findById(course.getId())).thenReturn(Optional.of(course));

        courseService.addNews(course.getId(), news);

        verify(repository).save(courseArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);

        assertTrue(courseArgumentCaptor.getValue().getNews().contains(news));
    }

    @ParameterizedTest
    @MethodSource("providedCourses")
    @DisplayName("7. Remove News to the Course")
    void removeNewsToCourse(Course course) {
        News newsToRemove = Objects.isNull(course.getNews()) ?
                Instancio.create(News.class) :
                course.getNews().stream()
                .findFirst().orElse(new News());

        when(repository.findById(course.getId())).thenReturn(Optional.of(course));
        when(newsService.findById(newsToRemove.getId())).thenReturn(newsToRemove);

        courseService.removeNews(course.getId(), newsToRemove.getId());

        verify(newsService).findById(newsToRemove.getId());
        verifyNoMoreInteractions(newsService);
        verify(repository).save(courseArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);

        assertFalse(courseArgumentCaptor.getValue().getNews().contains(newsToRemove));
    }

    @ParameterizedTest
    @MethodSource("providedCourses")
    @DisplayName("8. Add Shortcut to the Course")
    void addShortcutToCourse(Course course) {
        Shortcut shortcut = Instancio.create(Shortcut.class);

        when(repository.findById(course.getId())).thenReturn(Optional.of(course));

        courseService.addShortcut(course.getId(), shortcut);

        verify(repository).save(courseArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);

        assertTrue(courseArgumentCaptor.getValue().getShortcuts().contains(shortcut));
    }

    @ParameterizedTest
    @MethodSource("providedCourses")
    @DisplayName("9. Remove Shortcut to the Course")
    void removeShortcutToCourse(Course course) {
        Shortcut shortcutToRemove = Objects.isNull(course.getShortcuts())
                ? Instancio.create(Shortcut.class)
                : course.getShortcuts().stream()
                .findFirst().orElse(new Shortcut());

        when(repository.findById(course.getId())).thenReturn(Optional.of(course));
        when(shortcutService.findById(shortcutToRemove.getId())).thenReturn(shortcutToRemove);

        courseService.removeShortcut(course.getId(), shortcutToRemove.getId());

        verify(shortcutService).findById(shortcutToRemove.getId());
        verifyNoMoreInteractions(shortcutService);
        verify(repository).save(courseArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);

        assertFalse(courseArgumentCaptor.getValue().getShortcuts().contains(shortcutToRemove));
    }
}