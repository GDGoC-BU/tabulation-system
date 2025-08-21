package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Getter
class SegmentItem {
    String name;

    SegmentItem(String name) {
        this.name = name;
    }
}

@Component
public class SegmentSeeder implements CommandLineRunner {
    private final SegmentRepository repository;

    private final List<SegmentItem> segments = Arrays.asList(
            new SegmentItem("Swimwear"),
            new SegmentItem("Formal Attire"),
            new SegmentItem("Question and Answer"),
            new SegmentItem("Final Round")
    );

    @Autowired
    public SegmentSeeder(SegmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        segments.forEach(segmentItem -> {
            repository.save(new Segment(segmentItem.getName()));
        });
    }
}
