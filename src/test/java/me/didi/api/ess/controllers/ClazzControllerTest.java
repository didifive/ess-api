package me.didi.api.ess.controllers;

import me.didi.api.ess.dtos.requests.ClazzRequestDTO;
import me.didi.api.ess.dtos.responses.ClazzResponseDTO;
import me.didi.api.ess.dtos.responses.CourseResponseDTO;
import me.didi.api.ess.entities.Clazz;
import me.didi.api.ess.entities.Course;
import me.didi.api.ess.exceptions.RestExceptionHandler;
import me.didi.api.ess.services.ClazzService;
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

import java.util.List;

import static me.didi.api.ess.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.Is.isA;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Class Controller Tests")
@ExtendWith(MockitoExtension.class)
class ClazzControllerTest {

    public static final String PATH = "/api/v1/class";
    @Mock
    private ClazzService clazzService;
    @InjectMocks
    private ClazzController controller;

    private MockMvc mockMvc;
    private ClazzRequestDTO requestDTO;
    private Clazz clazz;
    private ClazzResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        Course course = Instancio.create(Course.class);
        requestDTO = Instancio.of(ClazzRequestDTO.class)
                .set(field("courseId"), course.getId())
                .create();
        clazz = Instancio.of(Clazz.class)
                .set(field("name"), requestDTO.name())
                .set(field("course"), course)
                .set(field("initDate"), requestDTO.initDate())
                .set(field("recoveryDate"), requestDTO.recoveryDate())
                .set(field("endDate"), requestDTO.endDate())
                .create();
        responseDTO = ClazzResponseDTO.toDto(clazz);
    }

    @Test
    @DisplayName("1. When POST is called should create Class")
    void whenPOSTIsCalledShouldCreateClass() throws Exception {

        when(clazzService.save(any(Clazz.class))).thenReturn(clazz);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpectAll(
                        status().isCreated()
                        , jsonPath("$.id", is(responseDTO.id()))
                        , jsonPath("$.name", is(responseDTO.name()))
                        , jsonPath("$.course.id", is(responseDTO.course().id()))
                        , jsonPath("$.course.name", is(responseDTO.course().name()))
                        , jsonPath("$.initDate", is(responseDTO.initDate().toString()))
                        , jsonPath("$.recoveryDate", is(responseDTO.recoveryDate().toString()))
                        , jsonPath("$.endDate", is(responseDTO.endDate().toString()))
                        , header().string("Location",
                                "http://localhost" + PATH + "/" + responseDTO.id())
                );
    }

    @Test
    @DisplayName("1.1. When POST is called with invalid data should return BadRequest")
    void whenPOSTIsCalledWithInvalidDataShouldReturnBadRequest() throws Exception {
        ClazzRequestDTO invalidClazzRequestDTO = Instancio.of(ClazzRequestDTO.class)
                .set(field("name"), null)
                .create();

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidClazzRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("2. When GET is called then return Clazz list")
    void whenGETIsCalledThenReturnClazzList() throws Exception {
        List<Clazz> clazzs = List.of(clazz);

        when(clazzService.findAll()).thenReturn(clazzs);

        mockMvc.perform(get(PATH))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.[0].id", is(responseDTO.id()))
                        , jsonPath("$.[0].name", is(responseDTO.name()))
                        , jsonPath("$.[0].course.id", is(responseDTO.course().id()))
                        , jsonPath("$.[0].course.name", is(responseDTO.course().name()))
                        , jsonPath("$.[0].initDate", is(responseDTO.initDate().toString()))
                        , jsonPath("$.[0].recoveryDate", is(responseDTO.recoveryDate().toString()))
                        , jsonPath("$.[0].endDate", is(responseDTO.endDate().toString()))
                );
    }

    @Test
    @DisplayName("3. When GET is called with Id parameter for Clazz")
    void whenGETIsCalledWithIdParameterForClazz() throws Exception {
        // when
        when(clazzService.findById(clazz.getId())).thenReturn(clazz);

        // then
        mockMvc.perform(get(PATH+"/"+clazz.getId()))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.id", is(responseDTO.id()))
                        , jsonPath("$.name", is(responseDTO.name()))
                        , jsonPath("$.course.id", is(responseDTO.course().id()))
                        , jsonPath("$.course.name", is(responseDTO.course().name()))
                        , jsonPath("$.initDate", is(responseDTO.initDate().toString()))
                        , jsonPath("$.recoveryDate", is(responseDTO.recoveryDate().toString()))
                        , jsonPath("$.endDate", is(responseDTO.endDate().toString()))
                );
    }
}