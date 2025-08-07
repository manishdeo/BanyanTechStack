package com.banyan.stack.controller;

import com.banyan.stack.entity.TvShow;
import com.banyan.stack.service.TvShowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TvShowController.class)
class TvShowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TvShowService tvShowService;

    @Test
    void getAllShows_ShouldReturnPagedResults() throws Exception {
        TvShow show = TvShow.builder()
                .id(1L)
                .name("Test Show")
                .type("Scripted")
                .build();

        Page<TvShow> page = new PageImpl<>(Arrays.asList(show), PageRequest.of(0, 20), 1);
        when(tvShowService.getAllShows(0, 20)).thenReturn(page);

        mockMvc.perform(get("/api/shows")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].name").value("Test Show"));
    }

    @Test
    void getShowById_ShouldReturnShow_WhenExists() throws Exception {
        TvShow show = TvShow.builder()
                .id(1L)
                .name("Test Show")
                .type("Scripted")
                .build();

        when(tvShowService.getShowById(1L)).thenReturn(Optional.of(show));

        mockMvc.perform(get("/api/shows/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Show"));
    }

    @Test
    void getShowById_ShouldReturn404_WhenNotExists() throws Exception {
        when(tvShowService.getShowById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/shows/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchShows_ShouldReturnResults() throws Exception {
        TvShow show = TvShow.builder()
                .id(1L)
                .name("Breaking Bad")
                .type("Scripted")
                .build();

        when(tvShowService.searchShows(anyString())).thenReturn(Arrays.asList(show));

        mockMvc.perform(get("/api/search/shows")
                        .param("q", "breaking"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Breaking Bad"));
    }
}