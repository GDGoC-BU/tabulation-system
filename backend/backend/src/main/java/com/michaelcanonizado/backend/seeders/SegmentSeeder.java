package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.models.SegmentStatus;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(2)
public class SegmentSeeder implements CommandLineRunner {
    private final SegmentRepository repository;

    private final List<Segment> segments = Arrays.asList(
            new Segment("Swimwear", 1),
            new Segment("Formal Attire", 2),
            new Segment("Question and Answer", 3),
            new Segment("Final Round", 4)
    );

    @Autowired
    public SegmentSeeder(SegmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        segments.forEach(repository::save);
    }
}
