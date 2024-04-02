package me.didi.api.ess.controllers;

import me.didi.api.ess.dtos.requests.RegistrationRequestDTO;
import me.didi.api.ess.dtos.responses.RegistrationResponseDTO;
import me.didi.api.ess.entities.Grade;
import me.didi.api.ess.entities.Registration;
import me.didi.api.ess.entities.pks.RegistrationId;
import me.didi.api.ess.exceptions.RestExceptionHandler;
import me.didi.api.ess.services.RegistrationService;
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

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static me.didi.api.ess.utils.JsonConvertionUtils.asJsonString;
import static me.didi.api.ess.utils.constants.GeneralConstantsUtils.DATE_PATTERN;
import static me.didi.api.ess.utils.constants.GeneralConstantsUtils.DATE_TIME_PATTERN;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Registration Controller Tests")
@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    public static final String PATH = "/api/v1/registration";
    @Mock
    private RegistrationService registrationService;
    @InjectMocks
    private RegistrationController controller;

    private MockMvc mockMvc;
    private RegistrationRequestDTO requestDTO;
    private Registration registration;
    private RegistrationResponseDTO responseDTO;
    private String registrationDateString;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        requestDTO = Instancio.create(RegistrationRequestDTO.class);
        registration = RegistrationRequestDTO.toEntity(requestDTO);
        registration.setId(Instancio.create(RegistrationId.class));
        registration.setGrades(Instancio.stream(Grade.class).limit(5).collect(Collectors.toSet()));
        responseDTO = RegistrationResponseDTO.toDto(registration);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        registrationDateString = responseDTO.registrationDate().format(formatter);
    }

    @Test
    @DisplayName("1. When POST is called should create Registration")
    void whenPOSTIsCalledShouldCreateRegistration() throws Exception {
        registration.setGrades(null);
        responseDTO = RegistrationResponseDTO.toDto(registration);

        when(registrationService.save(any(Registration.class))).thenReturn(registration);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpectAll(
                        status().isCreated()
                        , jsonPath("$.student.id", is(responseDTO.student().id()))
                        , jsonPath("$.class.id", is(responseDTO.clazz().id()))
                        , jsonPath("$.registrationDate", is(registrationDateString))
                        , jsonPath("$.status", is(responseDTO.status().toString()))
                        , jsonPath("$.subjects.*", hasSize(responseDTO.subjects().size()))
                        , jsonPath("$.grades").doesNotExist()
                        , header().string("Location",
                                "http://localhost" + PATH
                                        + "/student/" + responseDTO.student().id()
                                        + "/class/" + responseDTO.clazz().id())
                );
    }

    @Test
    @DisplayName("1.1. When POST is called with invalid data should return BadRequest")
    void whenPOSTIsCalledWithInvalidDataShouldReturnBadRequest() throws Exception {
        RegistrationRequestDTO invalidRegistrationRequestDTO = Instancio.of(RegistrationRequestDTO.class)
                .set(field("studentId"), null)
                .create();

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidRegistrationRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("2. When GET is called then return Registration list")
    void whenGETIsCalledThenReturnRegistrationList() throws Exception {
        List<Registration> registrations = List.of(registration);

        when(registrationService.findAll()).thenReturn(registrations);

        mockMvc.perform(get(PATH))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.[0].student.id", is(responseDTO.student().id()))
                        , jsonPath("$.[0].class.id", is(responseDTO.clazz().id()))
                        , jsonPath("$.[0].registrationDate", is(registrationDateString))
                        , jsonPath("$.[0].status", is(responseDTO.status().toString()))
                        , jsonPath("$.[0].subjects.*", hasSize(responseDTO.subjects().size()))
                        , jsonPath("$.[0].grades.*", hasSize(responseDTO.grades().size()))
                );
    }

    @Test
    @DisplayName("3. When GET is called with Id parameter for Registration")
    void whenGETIsCalledWithIdParameterForRegistration() throws Exception {
        // when
        when(registrationService.findById(
                registration.getId().getStudent().getId(),
                registration.getId().getClazz().getId()))
                .thenReturn(registration);

        // then
        mockMvc.perform(get(PATH
                        + "/student/" + registration.getId().getStudent().getId()
                        + "/class/" + registration.getId().getClazz().getId()))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.student.id", is(responseDTO.student().id()))
                        , jsonPath("$.class.id", is(responseDTO.clazz().id()))
                        , jsonPath("$.registrationDate", is(registrationDateString))
                        , jsonPath("$.status", is(responseDTO.status().toString()))
                        , jsonPath("$.subjects.*", hasSize(responseDTO.subjects().size()))
                        , jsonPath("$.grades.*", hasSize(responseDTO.grades().size()))
                );
    }
}