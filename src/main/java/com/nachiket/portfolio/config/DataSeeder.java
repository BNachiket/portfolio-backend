package com.nachiket.portfolio.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nachiket.portfolio.model.Portfolio;
import com.nachiket.portfolio.model.document.CertificationDoc;
import com.nachiket.portfolio.model.document.EducationDoc;
import com.nachiket.portfolio.model.document.ExperienceDoc;
import com.nachiket.portfolio.model.document.ProfileDoc;
import com.nachiket.portfolio.model.document.ProjectDoc;
import com.nachiket.portfolio.model.document.PublicationDoc;
import com.nachiket.portfolio.model.document.SkillDoc;
import com.nachiket.portfolio.repository.CertificationRepository;
import com.nachiket.portfolio.repository.EducationRepository;
import com.nachiket.portfolio.repository.ExperienceRepository;
import com.nachiket.portfolio.repository.ProfileRepository;
import com.nachiket.portfolio.repository.ProjectRepository;
import com.nachiket.portfolio.repository.PublicationRepository;
import com.nachiket.portfolio.repository.SkillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * One-time loader: copies portfolio-seed.json into the MongoDB collections.
 * <p>
 * Each collection is seeded only when it is empty, so edits made in MongoDB are never overwritten.
 * Set SEED_RESET=true for a single start to wipe the portfolio collections and reload from the JSON file.
 */
@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final ObjectMapper objectMapper;
    private final boolean reset;
    private final ProfileRepository profiles;
    private final SkillRepository skills;
    private final ExperienceRepository experience;
    private final ProjectRepository projects;
    private final EducationRepository education;
    private final CertificationRepository certifications;
    private final PublicationRepository publications;

    public DataSeeder(ObjectMapper objectMapper,
                      @Value("${app.seed.reset:false}") boolean reset,
                      ProfileRepository profiles,
                      SkillRepository skills,
                      ExperienceRepository experience,
                      ProjectRepository projects,
                      EducationRepository education,
                      CertificationRepository certifications,
                      PublicationRepository publications) {
        this.objectMapper = objectMapper;
        this.reset = reset;
        this.profiles = profiles;
        this.skills = skills;
        this.experience = experience;
        this.projects = projects;
        this.education = education;
        this.certifications = certifications;
        this.publications = publications;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (reset) {
            log.warn("SEED_RESET is true: clearing portfolio collections before seeding");
            profiles.deleteAll();
            skills.deleteAll();
            experience.deleteAll();
            projects.deleteAll();
            education.deleteAll();
            certifications.deleteAll();
            publications.deleteAll();
        }

        Portfolio p;
        try (InputStream in = new ClassPathResource("portfolio-seed.json").getInputStream()) {
            p = objectMapper.readValue(in, Portfolio.class);
        }

        if (profiles.count() == 0 && p.profile() != null) {
            var pr = p.profile();
            profiles.save(new ProfileDoc(ProfileDoc.MAIN_ID, pr.name(), pr.title(), pr.email(),
                    pr.phone(), pr.linkedin(), pr.photo(), pr.summary()));
            log.info("Seeded collection 'profile'");
        } else {
            log.info("Collection 'profile' already has data, skipping");
        }

        seed("skills", skills, indexed(p.skills(),
                (g, i) -> new SkillDoc("skill-" + (i + 1), g.category(), g.items(), i)));
        seed("experience", experience, indexed(p.experience(),
                (e, i) -> new ExperienceDoc("exp-" + (i + 1), e.role(), e.company(), e.period(), e.bullets(), i)));
        seed("projects", projects, indexed(p.projects(),
                (x, i) -> new ProjectDoc("project-" + (i + 1), x.name(), x.subtitle(), x.tags(), x.bullets(), i)));
        seed("education", education, indexed(p.education(),
                (e, i) -> new EducationDoc("edu-" + (i + 1), e.degree(), e.institution(), e.year(), e.score(), i)));
        seed("certifications", certifications, indexed(p.certifications(),
                (c, i) -> new CertificationDoc("cert-" + (i + 1), c, i)));
        seed("publications", publications, indexed(p.publications(),
                (c, i) -> new PublicationDoc("pub-" + (i + 1), c, i)));
    }

    private static <S, D> List<D> indexed(List<S> source, BiFunction<S, Integer, D> mapper) {
        List<D> out = new ArrayList<>();
        if (source != null) {
            for (int i = 0; i < source.size(); i++) {
                out.add(mapper.apply(source.get(i), i));
            }
        }
        return out;
    }

    private static <D> void seed(String collection, MongoRepository<D, String> repository, List<D> docs) {
        if (repository.count() > 0) {
            log.info("Collection '{}' already has data, skipping", collection);
            return;
        }
        repository.saveAll(docs);
        log.info("Seeded collection '{}' with {} document(s)", collection, docs.size());
    }
}
