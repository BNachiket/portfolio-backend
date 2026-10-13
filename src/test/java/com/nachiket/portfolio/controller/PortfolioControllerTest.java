package com.nachiket.portfolio.controller;

import com.nachiket.portfolio.model.ContactMessage;
import com.nachiket.portfolio.model.Portfolio;
import com.nachiket.portfolio.service.ContactService;
import com.nachiket.portfolio.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PortfolioController.class)
class PortfolioControllerTest {

    @Autowired MockMvc mvc;
    @MockBean PortfolioService portfolioService;
    @MockBean ContactService contactService;

    @Test
    void returnsPortfolio() throws Exception {
        var profile = new Portfolio.Profile("Test User", "Engineer", "a@b.com", "1", "", "/p.png", "Hi");
        when(portfolioService.getPortfolio()).thenReturn(
                new Portfolio(profile, List.of(), List.of(), List.of(), List.of(), List.of(), List.of()));

        mvc.perform(get("/api/portfolio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile.name").value("Test User"));
    }

    @Test
    void rejectsInvalidContact() throws Exception {
        mvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"email\":\"nope\",\"message\":\"short\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.message").exists());
    }

    @Test
    void savesValidContact() throws Exception {
        ContactMessage saved = new ContactMessage("Ann", "ann@x.com", "Hello there, nice site");
        when(contactService.save(any())).thenReturn(saved);

        mvc.perform(post("/api/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Ann\",\"email\":\"ann@x.com\",\"message\":\"Hello there, nice site\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("received"));
    }
}
