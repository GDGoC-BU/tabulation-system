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
            new CriterionItem("Interview Criterion 1", 5, "Interview Proper"),
            new CriterionItem("Interview Criterion 2", 5, "Interview Proper"),
            new CriterionItem("Interview Criterion 3", 5, "Interview Proper"),
            new CriterionItem("Beauty of Figure", 5, "Swimwear"),
            new CriterionItem("Stage Presence", 5, "Swimwear"),
            new CriterionItem("Poise and Personality", 5, "Swimwear"),
            new CriterionItem("Attire and Carriage", 5, "Formal Attire"),
            new CriterionItem("Stage Presence", 5, "Formal Attire"),
            new CriterionItem("Poise and Bearing", 5, "Formal Attire"),
            new CriterionItem("Intelligence", 5, "Question and Answer"),
            new CriterionItem("Poise and Personality", 5, "Question and Answer"),
            new CriterionItem("Intelligence and Wit", 5, "Final Round"),
            new CriterionItem("Poise, Confidence, and Personality", 5, "Final Round")
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
