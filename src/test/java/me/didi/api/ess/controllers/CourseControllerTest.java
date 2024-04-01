package me.didi.api.ess.controllers;

import me.didi.api.ess.dtos.requests.CourseRequestDTO;
import me.didi.api.ess.dtos.requests.MessageRequestDTO;
import me.didi.api.ess.dtos.requests.NewsRequestDTO;
import me.didi.api.ess.dtos.requests.ShortcutRequestDTO;
import me.didi.api.ess.dtos.responses.CourseResponseDTO;
import me.didi.api.ess.entities.Course;
import me.didi.api.ess.entities.Message;
import me.didi.api.ess.entities.News;
import me.didi.api.ess.entities.Shortcut;
import me.didi.api.ess.exceptions.RestExceptionHandler;
import me.didi.api.ess.services.CourseService;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.didi.api.ess.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.isA;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Course Controllers Tests")
@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    public static final String PATH = "/api/v1/course";
    @Mock
    private CourseService courseService;
    @InjectMocks
    private CourseController controller;

    private MockMvc mockMvc;
    private CourseRequestDTO requestDTO;
    private Course course;
    private CourseResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        requestDTO = Instancio.create(CourseRequestDTO.class);
        course = Instancio.of(Course.class)
                .set(field("name"), requestDTO.name())
                .set(field("frequency"), requestDTO.frequency())
                .create();
        responseDTO = CourseResponseDTO.toDto(course);
    }

    @Test
    @DisplayName("1. When POST is called should create Course")
    void whenPOSTIsCalledShouldCreateCourse() throws Exception {

        when(courseService.save(any(Course.class))).thenReturn(course);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpectAll(
                        status().isCreated()
                        , jsonPath("$.id", is(responseDTO.id()))
                        , jsonPath("$.name", is(responseDTO.name()))
                        , jsonPath("$.frequency", is(responseDTO.frequency().toString()))
                        , header().string("Location",
                                "http://localhost" + PATH + "/" + responseDTO.id())
                );
    }

    @Test
    @DisplayName("1.1. When POST is called with invalid data should return BadRequest")
    void whenPOSTIsCalledWithInvalidDataShouldReturnBadRequest() throws Exception {
        CourseRequestDTO invalidCourseRequestDTO = Instancio.of(CourseRequestDTO.class)
                .set(field("name"), null)
                .create();

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidCourseRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("2. When GET is called then return Course list")
    void whenGETIsCalledThenReturnCourseList() throws Exception {
        List<Course> courses = List.of(course);

        when(courseService.findAll()).thenReturn(courses);

        mockMvc.perform(get(PATH))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.[0].id", is(responseDTO.id()))
                        , jsonPath("$.[0].name", is(responseDTO.name()))
                        , jsonPath("$.[0].frequency", is(responseDTO.frequency().toString()))
                        , jsonPath("$.[0].messages.*", hasSize(responseDTO.messages().size()))
                        , jsonPath("$.[0].news.*", hasSize(responseDTO.news().size()))
                        , jsonPath("$.[0].shortcuts.*", hasSize(responseDTO.shortcuts().size()))
                );
    }

    @Test
    @DisplayName("3. When GET is called with Id parameter for Course")
    void whenGETIsCalledWithIdParameterForCourse() throws Exception {
        // when
        when(courseService.findById(course.getId())).thenReturn(course);

        // then
        mockMvc.perform(get(PATH + "/" + course.getId()))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.id", is(responseDTO.id()))
                        , jsonPath("$.name", is(responseDTO.name()))
                        , jsonPath("$.frequency", is(responseDTO.frequency().toString()))
                        , jsonPath("$.messages", isA(ArrayList.class))
                        , jsonPath("$.news", isA(ArrayList.class))
                        , jsonPath("$.shortcuts", isA(ArrayList.class))
                );
    }

    @Test
    @DisplayName("4. When POST is called to Add Message in Course")
    void whenPOSTIsCalledToAddMessageInCourse() throws Exception {
        MessageRequestDTO messageRequestDTO = Instancio.create(MessageRequestDTO.class);
        String messageId = UUID.randomUUID().toString();

        when(courseService.addMessage(
                eq(course.getId()),
                any(Message.class)))
                .thenReturn(messageId);

        mockMvc.perform(post(PATH + "/" + course.getId() + "/message/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(messageRequestDTO)))
                .andExpectAll(
                        status().isCreated()
                        , header().string("Location",
                                "http://localhost" + "/api/v1/message/" + messageId)
                );
    }

    @Test
    @DisplayName("4.1. When POST is called to Add Message in Course with invalid Message body should return BadRequest")
    void whenPOSTIsCalledToAddMessageInCourseWithInvalidMessageBodyShouldReturnBadRequest() throws Exception {
        MessageRequestDTO invalidMessageRequestDTO = Instancio.of(MessageRequestDTO.class)
                .set(field("icon"), null)
                .create();

        mockMvc.perform(post(PATH + "/" + course.getId() + "/message/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidMessageRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("5. When DELETE is called to Remove Message in Course")
    void whenDELETEIsCalledToRemoveMessageInCourse() throws Exception {
        String messageId = course.getMessages().stream().findFirst().orElse(new Message()).getId();

        doNothing().when(courseService)
                .removeMessage(course.getId(), messageId);

        mockMvc.perform(delete(PATH + "/" + course.getId() + "/message/" + messageId))
                .andExpect(status().isNoContent());

        verify(courseService).removeMessage(course.getId(), messageId);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    @DisplayName("6. When POST is called to Add News in Course")
    void whenPOSTIsCalledToAddNewsInCourse() throws Exception {
        NewsRequestDTO newsRequestDTO = Instancio.create(NewsRequestDTO.class);
        String newsId = UUID.randomUUID().toString();

        when(courseService.addNews(
                eq(course.getId()),
                any(News.class)))
                .thenReturn(newsId);

        mockMvc.perform(post(PATH + "/" + course.getId() + "/news/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newsRequestDTO)))
                .andExpectAll(
                        status().isCreated()
                        , header().string("Location",
                                "http://localhost" + "/api/v1/news/" + newsId)
                );
    }

    @Test
    @DisplayName("6.1. When POST is called to Add News in Course with invalid News body should return BadRequest")
    void whenPOSTIsCalledToAddNewsInCourseWithInvalidNewsBodyShouldReturnBadRequest() throws Exception {
        NewsRequestDTO invalidNewsRequestDTO = Instancio.of(NewsRequestDTO.class)
                .set(field("icon"), null)
                .create();

        mockMvc.perform(post(PATH + "/" + course.getId() + "/news/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidNewsRequestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("7. When DELETE is called to Remove News in Course")
    void whenDELETEIsCalledToRemoveNewsInCourse() throws Exception {
        String newsId = course.getNews().stream().findFirst().orElse(new News()).getId();

        doNothing().when(courseService)
                .removeNews(course.getId(), newsId);

        mockMvc.perform(delete(PATH + "/" + course.getId() + "/news/" + newsId))
                .andExpect(status().isNoContent());

        verify(courseService).removeNews(course.getId(), newsId);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    @DisplayName("8. When POST is called to Add Shortcut in Course")
    void whenPOSTIsCalledToAddShortcutInCourse() throws Exception {
        ShortcutRequestDTO shortcutRequestDTO = Instancio.create(ShortcutRequestDTO.class);
        String shortcutId = UUID.randomUUID().toString();

        when(courseService.addShortcut(
                eq(course.getId()),
                any(Shortcut.class)))
                .thenReturn(shortcutId);

        mockMvc.perform(post(PATH + "/" + course.getId() + "/shortcut/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(shortcutRequestDTO)))
                .andExpectAll(
                        status().isCreated()
                        , header().string("Location",
                                "http://localhost" + "/api/v1/shortcut/" + shortcutId)
                );
    }

    @Test
    @DisplayName("8.1. When POST is called to Add Shortcut in Course with invalid Shortcut body should return BadRequest")
    void whenPOSTIsCalledToAddShortcutInCourseWithInvalidShortcutBodyShouldReturnBadRequest() throws Exception {
        ShortcutRequestDTO invalidShortcutRequestDTO = Instancio.of(ShortcutRequestDTO.class)
                .set(field("icon"), null)
                .create();

        mockMvc.perform(post(PATH + "/" + course.getId() + "/shortcut/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidShortcutRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("9. When DELETE is called to Remove Shortcut in Course")
    void whenDELETEIsCalledToRemoveShortcutInCourse() throws Exception {
        String shortcutId = course.getShortcuts().stream().findFirst().orElse(new Shortcut()).getId();

        doNothing().when(courseService)
                .removeShortcut(course.getId(), shortcutId);

        mockMvc.perform(delete(PATH + "/" + course.getId() + "/shortcut/" + shortcutId))
                .andExpect(status().isNoContent());

        verify(courseService).removeShortcut(course.getId(), shortcutId);
        verifyNoMoreInteractions(courseService);
    }
}