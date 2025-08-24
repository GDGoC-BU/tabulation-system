package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.CriterionRepository;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
@Order(3)
public class CriterionSeeder implements CommandLineRunner {
    private final SegmentRepository segmentRepository;

    private final List<CriterionItem> criteria = Arrays.asList(
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
    public CriterionSeeder(SegmentRepository segmentRepository) {
        this.segmentRepository = segmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Segment> segments = segmentRepository.findAll();
        segments.forEach(segment -> {
            criteria.forEach(criterionItem -> {
                if (segment.getName().equals(criterionItem.getSegment())) {
                    segment.addCriteria(new Criterion(criterionItem.getName(), criterionItem.getMaxScore(), segment));
                }
            });
            segmentRepository.save(segment);
        });
    }
}
