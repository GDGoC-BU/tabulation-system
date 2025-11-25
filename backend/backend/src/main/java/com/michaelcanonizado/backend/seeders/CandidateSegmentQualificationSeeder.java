package com.michaelcanonizado.backend.seeders;

import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.CandidateSegmentQualification;
import com.michaelcanonizado.backend.models.Segment;
import com.michaelcanonizado.backend.repositories.CandidateRepository;
import com.michaelcanonizado.backend.repositories.CandidateSegmentQualificationRepository;
import com.michaelcanonizado.backend.repositories.SegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CandidateSegmentQualificationSeeder implements DatabaseSeeder {
    @Autowired
    private CandidateSegmentQualificationRepository csqRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private SegmentRepository segmentRepository;


    @Transactional
    @Override
    public void seed() {
        List<Candidate> candidates = candidateRepository.findAll();
        List<Segment> segments = segmentRepository.findAll();

        candidates.forEach(candidate -> {
            segments.forEach(segment -> {
                CandidateSegmentQualification csq = new CandidateSegmentQualification(segment, candidate);
                segment.addCandidateQualification(csq);
                candidate.addCandidateSegmentQualification(csq);

                csqRepository.save(csq);
                segmentRepository.save(segment);
            });
            candidateRepository.save(candidate);
        });
    }
}
