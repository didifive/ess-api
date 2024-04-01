package me.didi.api.ess.controllers;

import me.didi.api.ess.dtos.requests.SubjectRequestDTO;
import me.didi.api.ess.dtos.responses.SubjectResponseDTO;
import me.didi.api.ess.entities.Subject;
import me.didi.api.ess.exceptions.RestExceptionHandler;
import me.didi.api.ess.services.SubjectService;
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
import java.util.UUID;

import static me.didi.api.ess.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.CoreMatchers.is;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Subject Controller Tests")
@ExtendWith(MockitoExtension.class)
class SubjectControllerTest {

    public static final String PATH = "/api/v1/subject";
    @Mock
    private SubjectService subjectService;
    @InjectMocks
    private SubjectController controller;

    private MockMvc mockMvc;
    private SubjectRequestDTO requestDTO;
    private Subject subject;
    private SubjectResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        requestDTO = Instancio.create(SubjectRequestDTO.class);
        subject = SubjectRequestDTO.toEntity(requestDTO);
        subject.setId(UUID.randomUUID().toString());
        responseDTO = SubjectResponseDTO.toDto(subject);
    }

    @Test
    @DisplayName("1. When POST is called should create Subject")
    void whenPOSTIsCalledShouldCreateSubject() throws Exception {

        when(subjectService.save(any(Subject.class))).thenReturn(subject);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpectAll(
                        status().isCreated()
                        , jsonPath("$.icon", is(responseDTO.icon()))
                        , jsonPath("$.title", is(responseDTO.title()))
                        , jsonPath("$.description", is(responseDTO.description()))
                        , header().string("Location",
                                "http://localhost"+ PATH + "/" + responseDTO.id())
                );
    }

    @Test
    @DisplayName("1.1. When POST is called with invalid data should return BadRequest")
    void whenPOSTIsCalledWithInvalidDataShouldReturnBadRequest() throws Exception {
        SubjectRequestDTO invalidSubjectRequestDTO = Instancio.of(SubjectRequestDTO.class)
                .set(field("description"), null)
                .create();

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidSubjectRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("2. When GET is called then return Subject list")
    void whenGETIsCalledThenReturnSubjectList() throws Exception {
        List<Subject> subjects = List.of(subject);

        when(subjectService.findAll()).thenReturn(subjects);

        mockMvc.perform(get(PATH))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.[0].icon", is(responseDTO.icon()))
                        , jsonPath("$.[0].title", is(responseDTO.title()))
                        , jsonPath("$.[0].description", is(responseDTO.description()))
                );
    }

    @Test
    @DisplayName("3. When GET is called with Id parameter for Subject")
    void whenGETIsCalledWithIdParameterForSubject() throws Exception {
        // when
        when(subjectService.findById(subject.getId())).thenReturn(subject);

        // then
        mockMvc.perform(get(PATH+"/"+subject.getId()))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.icon", is(responseDTO.icon()))
                        , jsonPath("$.title", is(responseDTO.title()))
                        , jsonPath("$.description", is(responseDTO.description()))
                );
    }
}