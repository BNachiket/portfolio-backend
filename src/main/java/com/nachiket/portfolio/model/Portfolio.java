package com.nachiket.portfolio.model;

import java.util.List;

/** Portfolio content as served by the API (assembled from the MongoDB collections). */
public record Portfolio(
        Profile profile,
        List<SkillGroup> skills,
        List<Experience> experience,
        List<Project> projects,
        List<Education> education,
        List<String> certifications,
        List<String> publications) {

    public record Profile(String name, String title, String email, String phone,
                          String linkedin, String photo, String summary) {}

    public record SkillGroup(String category, List<String> items) {}

    public record Experience(String role, String company, String period, List<String> bullets) {}

    public record Project(String name, String subtitle, List<String> tags, List<String> bullets) {}

    public record Education(String degree, String institution, String year, String score) {}
}
