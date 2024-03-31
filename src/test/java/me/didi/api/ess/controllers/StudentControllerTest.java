package me.didi.api.ess.controllers;

import me.didi.api.ess.dtos.requests.StudentRequestDTO;
import me.didi.api.ess.dtos.responses.StudentResponseDTO;
import me.didi.api.ess.entities.Student;
import me.didi.api.ess.exceptions.RestExceptionHandler;
import me.didi.api.ess.services.StudentService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Student Controllers Tests")
@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    public static final String PATH = "/api/v1/student";
    @Mock
    private StudentService studentService;
    @InjectMocks
    private StudentController controller;

    private MockMvc mockMvc;
    private StudentRequestDTO requestDTO;
    private Student student;
    private StudentResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        requestDTO = Instancio.create(StudentRequestDTO.class);
        student = Instancio.of(Student.class)
                .set(field("givenName"), requestDTO.givenName())
                .set(field("familyName"), requestDTO.familyName())
                .set(field("guardian"), requestDTO.guardian())
                .set(field("photo"), requestDTO.photo())
                .create();
        responseDTO = StudentResponseDTO.toDto(student);
    }

    @Test
    @DisplayName("1. When POST is called should create Student")
    void whenPOSTIsCalledShouldCreateStudent() throws Exception {

        when(studentService.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpectAll(
                        status().isCreated()
                        , jsonPath("$.id", is(responseDTO.id()))
                        , jsonPath("$.name", is(responseDTO.name()))
                        , jsonPath("$.guardian", is(responseDTO.guardian()))
                        , jsonPath("$.photo", is(responseDTO.photo()))
                        , header().string("Location",
                                "http://localhost"+ PATH + "/" + responseDTO.id())
                );
    }

    @Test
    @DisplayName("1.1. When POST is called with invalid data should return BadRequest")
    void whenPOSTIsCalledWithInvalidDataShouldReturnBadRequest() throws Exception {
        StudentRequestDTO invalidStudentRequestDTO = Instancio.of(StudentRequestDTO.class)
                .set(field("givenName"), null)
                .create();

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidStudentRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("2. When GET is called then return Student list")
    void whenGETIsCalledThenReturnStudentList() throws Exception {
        List<Student> students = List.of(student);

        when(studentService.findAll()).thenReturn(students);

        mockMvc.perform(get(PATH))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.[0].id", is(responseDTO.id()))
                        , jsonPath("$.[0].name", is(responseDTO.name()))
                        , jsonPath("$.[0].guardian", is(responseDTO.guardian()))
                        , jsonPath("$.[0].photo", is(responseDTO.photo()))
                );
    }

    @Test
    @DisplayName("3. When GET is called with Id parameter for Student")
    void whenGETIsCalledWithIdParameterForStudent() throws Exception {
        // when
        when(studentService.findById(student.getId())).thenReturn(student);

        // then
        mockMvc.perform(get(PATH+"/"+student.getId()))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.id", is(responseDTO.id()))
                        , jsonPath("$.name", is(responseDTO.name()))
                        , jsonPath("$.guardian", is(responseDTO.guardian()))
                        , jsonPath("$.photo", is(responseDTO.photo()))
                );
    }
}