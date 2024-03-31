package me.didi.api.ess.controllers;

import me.didi.api.ess.dtos.responses.MessageResponseDTO;
import me.didi.api.ess.entities.Message;
import me.didi.api.ess.exceptions.RestExceptionHandler;
import me.didi.api.ess.services.MessageService;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static me.didi.api.ess.utils.constants.ConstantsUtils.DATE_TIME_PATTERN;
import static org.hamcrest.CoreMatchers.is;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Message Controllers Tests")
@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    public static final String PATH = "/api/v1/message";
    @Mock
    private MessageService messageService;
    @InjectMocks
    private MessageController controller;

    private MockMvc mockMvc;
    private Message message;
    private MessageResponseDTO responseDTO;
    private String dateTimeString;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        message = Instancio.create(Message.class);
        responseDTO = MessageResponseDTO.toDto(message);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        dateTimeString = responseDTO.dateTime().format(formatter);
    }

    @Test
    @DisplayName("1. When GET is called then return Message list")
    void whenGETIsCalledThenReturnMessageList() throws Exception {
        List<Message> messages = List.of(message);

        when(messageService.findAll()).thenReturn(messages);

        mockMvc.perform(get(PATH))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.[0].id", is(responseDTO.id()))
                        , jsonPath("$.[0].icon", is(responseDTO.icon()))
                        , jsonPath("$.[0].title", is(responseDTO.title()))
                        , jsonPath("$.[0].description", is(responseDTO.description()))
                        , jsonPath("$.[0].dateTime", is(dateTimeString))
                );
    }

    @Test
    @DisplayName("2. When GET is called with Id parameter for Message")
    void whenGETIsCalledWithIdParameterForMessage() throws Exception {
        // when
        when(messageService.findById(message.getId())).thenReturn(message);

        // then
        mockMvc.perform(get(PATH+"/"+message.getId()))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.id", is(responseDTO.id()))
                        , jsonPath("$.icon", is(responseDTO.icon()))
                        , jsonPath("$.title", is(responseDTO.title()))
                        , jsonPath("$.description", is(responseDTO.description()))
                        , jsonPath("$.dateTime", is(dateTimeString))
                );
    }
}