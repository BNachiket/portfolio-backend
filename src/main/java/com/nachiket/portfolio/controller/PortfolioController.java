package com.nachiket.portfolio.controller;

import com.nachiket.portfolio.model.ContactRequest;
import com.nachiket.portfolio.model.ContactResponse;
import com.nachiket.portfolio.model.Portfolio;
import com.nachiket.portfolio.service.ContactService;
import com.nachiket.portfolio.service.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final ContactService contactService;

    public PortfolioController(PortfolioService portfolioService, ContactService contactService) {
        this.portfolioService = portfolioService;
        this.contactService = contactService;
    }

    /** Lightweight check for hosting platforms (does not touch the database). */
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @GetMapping("/portfolio")
    public Portfolio portfolio() {
        return portfolioService.getPortfolio();
    }

    @GetMapping("/profile")
    public Portfolio.Profile profile() {
        return portfolioService.getProfile();
    }

    @GetMapping("/skills")
    public List<Portfolio.SkillGroup> skills() {
        return portfolioService.getSkills();
    }

    @GetMapping("/experience")
    public List<Portfolio.Experience> experience() {
        return portfolioService.getExperience();
    }

    @GetMapping("/projects")
    public List<Portfolio.Project> projects() {
        return portfolioService.getProjects();
    }

    @GetMapping("/education")
    public List<Portfolio.Education> education() {
        return portfolioService.getEducation();
    }

    @GetMapping("/certifications")
    public List<String> certifications() {
        return portfolioService.getCertifications();
    }

    @GetMapping("/publications")
    public List<String> publications() {
        return portfolioService.getPublications();
    }

    @PostMapping("/contact")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactResponse contact(@Valid @RequestBody ContactRequest request) {
        var saved = contactService.save(request);
        return new ContactResponse(saved.getId(), "received");
    }
}
