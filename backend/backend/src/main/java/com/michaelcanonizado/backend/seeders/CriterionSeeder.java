package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.CriterionRepository;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
class CriterionItem {
    private String name;
    private int maxScore;
    private String segment;
}

@Component
public class CriterionSeeder implements DatabaseSeeder {
    private final SegmentRepository segmentRepository;
    private final CriterionRepository criterionRepository;

    private final List<CriterionItem> criteria = Arrays.asList(
            /* Coronation Night Criterion */
            new CriterionItem("Stage Presence", 35, "Production Number"),
            new CriterionItem("Performance And Execution", 30, "Production Number"),
            new CriterionItem("Projection and Personality", 25, "Production Number"),
            new CriterionItem("Overall Impact", 10, "Production Number"),

            new CriterionItem("Presence and Projection", 25, "Swimwear"),
            new CriterionItem("Poise", 20, "Swimwear"),
            new CriterionItem("Physical Well-being and Vitality", 5, "Swimwear"),

            new CriterionItem("Attire, Elegance, and Grace", 30, "Formal Attire"),
            new CriterionItem("Stage Presence", 20, "Formal Attire"),

            new CriterionItem("Intelligence", 50, "Preliminary Question and Answer"),
            new CriterionItem("Alignment with Advocacy or Purpose", 25, "Preliminary Question and Answer"),
            new CriterionItem("Poise and Personality", 25, "Preliminary Question and Answer"),

            new CriterionItem("Intelligence", 50, "Final Question and Answer"),
            new CriterionItem("Relevance", 25, "Final Question and Answer"),
            new CriterionItem("Poise and Personality", 25, "Final Question and Answer"),

            /* Closed Door Interview Criterion */
            new CriterionItem("Presence and Projection", 15, "Swimwear"),
            new CriterionItem("Poise", 10, "Swimwear"),
            new CriterionItem("Physical Well-being and Vitality", 5, "Swimwear"),

            new CriterionItem("Attire, Elegance, and Grace", 20, "Formal Attire"),
            new CriterionItem("Stage Presence", 10, "Formal Attire"),

            new CriterionItem("Intelligence", 20, "Question & Answer"),
            new CriterionItem("Poise and Personality", 20, "Question & Answer")


    );

    @Autowired
    public CriterionSeeder(SegmentRepository segmentRepository, CriterionRepository criterionRepository) {
        this.segmentRepository = segmentRepository;
        this.criterionRepository = criterionRepository;
    }

    @Transactional
    @Override
    public void seed() {
        List<Segment> segments = segmentRepository.findAll();

        segments.forEach(segment -> {
            criteria.forEach(criterionItem -> {
                if (segment.getName().equals(criterionItem.getSegment())) {
                    Criterion criterion = new Criterion(
                            criterionItem.getName(),
                            criterionItem.getMaxScore(),
                            segment
                    );
                    segment.addCriterion(criterion);
                    criterionRepository.save(criterion);
                }
            });
            segmentRepository.save(segment);
        });
    }
}
