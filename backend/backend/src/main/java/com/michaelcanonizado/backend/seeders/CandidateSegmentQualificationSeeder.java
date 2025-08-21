package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.CandidateSegmentQualification;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.CandidateRepository;
import com.michaelcanonizado.backend.repositories.CandidateSegmentQualificationRepository;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(6)
public class CandidateSegmentQualificationSeeder implements CommandLineRunner {
    private final CandidateSegmentQualificationRepository csqRepository;
    private final CandidateRepository candidateRepository;
    private final SegmentRepository segmentRepository;

    @Autowired
    public CandidateSegmentQualificationSeeder(CandidateSegmentQualificationRepository csqRepository, CandidateRepository candidateRepository, SegmentRepository segmentRepository) {
        this.csqRepository = csqRepository;
        this.candidateRepository = candidateRepository;
        this.segmentRepository = segmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Candidate> candidates = candidateRepository.findAll();
        List<Segment> segments = segmentRepository.findAll();

        candidates.forEach(candidate -> {
            csqRepository.save(new CandidateSegmentQualification(segments.get(0), candidate));
            csqRepository.save(new CandidateSegmentQualification(segments.get(1), candidate));
            csqRepository.save(new CandidateSegmentQualification(segments.get(2), candidate));
            csqRepository.save(new CandidateSegmentQualification(segments.get(3), candidate));
        });
    }
}
