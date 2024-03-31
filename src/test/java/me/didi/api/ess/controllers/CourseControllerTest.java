package me.didi.api.ess.controllers;

import me.didi.api.ess.dtos.requests.CourseRequestDTO;
import me.didi.api.ess.dtos.responses.CourseResponseDTO;
import me.didi.api.ess.entities.Course;
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

import static me.didi.api.ess.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.isA;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                                "http://localhost"+ PATH + "/" + responseDTO.id())
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
                        , jsonPath("$.[0].shortcuts.*",  hasSize(responseDTO.shortcuts().size()))
                );
    }

    @Test
    @DisplayName("3. When GET is called with Id parameter for Course")
    void whenGETIsCalledWithIdParameterForCourse() throws Exception {
        // when
        when(courseService.findById(course.getId())).thenReturn(course);

        // then
        mockMvc.perform(get(PATH+"/"+course.getId()))
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
}