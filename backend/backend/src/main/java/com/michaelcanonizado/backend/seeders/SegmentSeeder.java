package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(2)
public class SegmentSeeder implements CommandLineRunner {
    private final SegmentRepository segmentRepository;
    private final PageantRepository pageantRepository;

    @Autowired
    public SegmentSeeder(SegmentRepository segmentRepository, PageantRepository pageantRepository) {
        this.segmentRepository = segmentRepository;
        this.pageantRepository = pageantRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Pageant pageant = pageantRepository.findAll().getFirst();

        List<Segment> segments = Arrays.asList(
                new Segment("Swimwear", 1, pageant),
                new Segment("Formal Attire", 2, pageant),
                new Segment("Question and Answer", 3, pageant),
                new Segment("Final Round", 4, pageant)
        );

        segments.forEach(segmentRepository::save);
    }
}
