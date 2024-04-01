package me.didi.api.ess.controllers;

import me.didi.api.ess.dtos.responses.NewsResponseDTO;
import me.didi.api.ess.entities.News;
import me.didi.api.ess.exceptions.RestExceptionHandler;
import me.didi.api.ess.services.NewsService;
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
@DisplayName("News Controller Tests")
@ExtendWith(MockitoExtension.class)
class NewsControllerTest {

    public static final String PATH = "/api/v1/news";
    @Mock
    private NewsService newsService;
    @InjectMocks
    private NewsController controller;

    private MockMvc mockMvc;
    private News news;
    private NewsResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        news = Instancio.create(News.class);
        responseDTO = NewsResponseDTO.toDto(news);
    }

    @Test
    @DisplayName("1. When GET is called then return News list")
    void whenGETIsCalledThenReturnNewsList() throws Exception {
        List<News> newss = List.of(news);

        when(newsService.findAll()).thenReturn(newss);

        mockMvc.perform(get(PATH))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.[0].id", is(responseDTO.id()))
                        , jsonPath("$.[0].icon", is(responseDTO.icon()))
                        , jsonPath("$.[0].title", is(responseDTO.title()))
                        , jsonPath("$.[0].description", is(responseDTO.description()))
                );
    }

    @Test
    @DisplayName("2. When GET is called with Id parameter for News")
    void whenGETIsCalledWithIdParameterForNews() throws Exception {
        // when
        when(newsService.findById(news.getId())).thenReturn(news);

        // then
        mockMvc.perform(get(PATH + "/" + news.getId()))
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.id", is(responseDTO.id()))
                        , jsonPath("$.icon", is(responseDTO.icon()))
                        , jsonPath("$.title", is(responseDTO.title()))
                        , jsonPath("$.description", is(responseDTO.description()))
                );
    }
}