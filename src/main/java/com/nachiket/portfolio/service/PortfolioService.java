package com.nachiket.portfolio.service;

import com.nachiket.portfolio.model.Portfolio;
import com.nachiket.portfolio.model.document.ProfileDoc;
import com.nachiket.portfolio.repository.CertificationRepository;
import com.nachiket.portfolio.repository.EducationRepository;
import com.nachiket.portfolio.repository.ExperienceRepository;
import com.nachiket.portfolio.repository.ProfileRepository;
import com.nachiket.portfolio.repository.ProjectRepository;
import com.nachiket.portfolio.repository.PublicationRepository;
import com.nachiket.portfolio.repository.SkillRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Reads every section of the portfolio from its MongoDB collection. */
@Service
public class PortfolioService {

    private final ProfileRepository profiles;
    private final SkillRepository skills;
    private final ExperienceRepository experience;
    private final ProjectRepository projects;
    private final EducationRepository education;
    private final CertificationRepository certifications;
    private final PublicationRepository publications;

    public PortfolioService(ProfileRepository profiles,
                            SkillRepository skills,
                            ExperienceRepository experience,
                            ProjectRepository projects,
                            EducationRepository education,
                            CertificationRepository certifications,
                            PublicationRepository publications) {
        this.profiles = profiles;
        this.skills = skills;
        this.experience = experience;
        this.projects = projects;
        this.education = education;
        this.certifications = certifications;
        this.publications = publications;
    }

    public Portfolio getPortfolio() {
        return new Portfolio(
                getProfile(),
                getSkills(),
                getExperience(),
                getProjects(),
                getEducation(),
                getCertifications(),
                getPublications());
    }

    public Portfolio.Profile getProfile() {
        return profiles.findById(ProfileDoc.MAIN_ID)
                .map(p -> new Portfolio.Profile(
                        p.name(), p.title(), p.email(), p.phone(), p.linkedin(), p.photo(), p.summary()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profile not found in MongoDB (has the database been seeded?)"));
    }

    public List<Portfolio.SkillGroup> getSkills() {
        return skills.findAllByOrderByPositionAsc().stream()
                .map(s -> new Portfolio.SkillGroup(s.category(), s.items()))
                .toList();
    }

    public List<Portfolio.Experience> getExperience() {
        return experience.findAllByOrderByPositionAsc().stream()
                .map(e -> new Portfolio.Experience(e.role(), e.company(), e.period(), e.bullets()))
                .toList();
    }

    public List<Portfolio.Project> getProjects() {
        return projects.findAllByOrderByPositionAsc().stream()
                .map(p -> new Portfolio.Project(p.name(), p.subtitle(), p.tags(), p.bullets()))
                .toList();
    }

    public List<Portfolio.Education> getEducation() {
        return education.findAllByOrderByPositionAsc().stream()
                .map(e -> new Portfolio.Education(e.degree(), e.institution(), e.year(), e.score()))
                .toList();
    }

    public List<String> getCertifications() {
        return certifications.findAllByOrderByPositionAsc().stream()
                .map(c -> c.title())
                .toList();
    }

    public List<String> getPublications() {
        return publications.findAllByOrderByPositionAsc().stream()
                .map(p -> p.citation())
                .toList();
    }
}
