package me.didi.api.ess.controllers;

import me.didi.api.ess.dtos.requests.GradeRequestDTO;
import me.didi.api.ess.dtos.responses.GradeResponseDTO;
import me.didi.api.ess.entities.Grade;
import me.didi.api.ess.entities.pks.GradeId;
import me.didi.api.ess.exceptions.RestExceptionHandler;
import me.didi.api.ess.services.GradeService;
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
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Grade Controller Tests")
@ExtendWith(MockitoExtension.class)
class GradeControllerTest {

    public static final String PATH = "/api/v1/grade";
    @Mock
    private GradeService gradeService;
    @InjectMocks
    private GradeController controller;

    private MockMvc mockMvc;
    private GradeRequestDTO requestDTO;
    private Grade grade;
    private GradeResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        requestDTO = Instancio.create(GradeRequestDTO.class);
        grade = GradeRequestDTO.toEntity(requestDTO);
        grade.setId(Instancio.create(GradeId.class));
        responseDTO = GradeResponseDTO.toDto(grade);
    }

    @Test
    @DisplayName("1. When POST is called should create Grade")
    void whenPOSTIsCalledShouldCreateGrade() throws Exception {
        when(gradeService.save(any(Grade.class))).thenReturn(grade);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpectAll(
                        status().isCreated()
                        , jsonPath("$.student.id", is(responseDTO.student().id()))
                        , jsonPath("$.class.id", is(responseDTO.clazz().id()))
                        , jsonPath("$.subject.id", is(responseDTO.subject().id()))
                        , jsonPath("$.gradeType", is(responseDTO.gradeType().toString()))
                        , jsonPath("$.value", is(responseDTO.value().doubleValue()))
                        , header().string("Location",
                                "http://localhost" + PATH
                                        + "/student/" + responseDTO.student().id()
                                        + "/class/" + responseDTO.clazz().id()
                                        + "/subject/" + responseDTO.subject().id())
                );
    }

    @Test
    @DisplayName("1.1. When POST is called with invalid data should return BadRequest")
    void whenPOSTIsCalledWithInvalidDataShouldReturnBadRequest() throws Exception {
        GradeRequestDTO invalidGradeRequestDTO = Instancio.of(GradeRequestDTO.class)
                .set(field("studentId"), null)
                .create();

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidGradeRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("2. When GET is called then return Grade list")
    void whenGETIsCalledThenReturnGradeList() throws Exception {
        List<Grade> grades = List.of(grade);

        when(gradeService.findAll()).thenReturn(grades);

        mockMvc.perform(get(PATH))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.[0].student.id", is(responseDTO.student().id()))
                        , jsonPath("$.[0].class.id", is(responseDTO.clazz().id()))
                        , jsonPath("$.[0].subject.id", is(responseDTO.subject().id()))
                        , jsonPath("$.[0].gradeType", is(responseDTO.gradeType().toString()))
                        , jsonPath("$.[0].value", is(responseDTO.value().doubleValue()))
                );
    }

    @Test
    @DisplayName("3. When GET is called with Id parameter for Grade")
    void whenGETIsCalledWithIdParameterForGrade() throws Exception {
        // when
        when(gradeService.findById(
                grade.getId().getRegistration().getId().getStudent().getId(),
                grade.getId().getRegistration().getId().getClazz().getId(),
                grade.getId().getSubject().getId()))
                .thenReturn(grade);

        // then
        mockMvc.perform(get(PATH
                        + "/student/" + grade.getId().getRegistration().getId().getStudent().getId()
                        + "/class/" + grade.getId().getRegistration().getId().getClazz().getId()
                        + "/subject/" + grade.getId().getSubject().getId()))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.student.id", is(responseDTO.student().id()))
                        , jsonPath("$.class.id", is(responseDTO.clazz().id()))
                        , jsonPath("$.subject.id", is(responseDTO.subject().id()))
                        , jsonPath("$.gradeType", is(responseDTO.gradeType().toString()))
                        , jsonPath("$.value", is(responseDTO.value().doubleValue()))
                );
    }
}