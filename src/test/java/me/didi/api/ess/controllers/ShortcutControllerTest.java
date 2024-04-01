package me.didi.api.ess.controllers;

import me.didi.api.ess.dtos.responses.ShortcutResponseDTO;
import me.didi.api.ess.entities.Shortcut;
import me.didi.api.ess.exceptions.RestExceptionHandler;
import me.didi.api.ess.services.ShortcutService;
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

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Shortcut Controller Tests")
@ExtendWith(MockitoExtension.class)
class ShortcutControllerTest {

    public static final String PATH = "/api/v1/shortcut";
    @Mock
    private ShortcutService shortcutService;
    @InjectMocks
    private ShortcutController controller;

    private MockMvc mockMvc;
    private Shortcut shortcut;
    private ShortcutResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        shortcut = Instancio.create(Shortcut.class);
        responseDTO = ShortcutResponseDTO.toDto(shortcut);
    }

    @Test
    @DisplayName("1. When GET is called then return Shortcut list")
    void whenGETIsCalledThenReturnShortcutList() throws Exception {
        List<Shortcut> shortcuts = List.of(shortcut);

        when(shortcutService.findAll()).thenReturn(shortcuts);

        mockMvc.perform(get(PATH))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.[0].id", is(responseDTO.id()))
                        , jsonPath("$.[0].icon", is(responseDTO.icon()))
                        , jsonPath("$.[0].title", is(responseDTO.title()))
                        , jsonPath("$.[0].description", is(responseDTO.description()))
                        , jsonPath("$.[0].link", is(responseDTO.link()))
                );
    }

    @Test
    @DisplayName("2. When GET is called with Id parameter for Shortcut")
    void whenGETIsCalledWithIdParameterForShortcut() throws Exception {
        // when
        when(shortcutService.findById(shortcut.getId())).thenReturn(shortcut);

        // then
        mockMvc.perform(get(PATH + "/" + shortcut.getId()))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.id", is(responseDTO.id()))
                        , jsonPath("$.icon", is(responseDTO.icon()))
                        , jsonPath("$.title", is(responseDTO.title()))
                        , jsonPath("$.description", is(responseDTO.description()))
                        , jsonPath("$.link", is(responseDTO.link()))
                );
    }
}