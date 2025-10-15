package com.michaelcanonizado.backend.specifications;

import com.michaelcanonizado.backend.models.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public class CandidateSegmentQualificationSpecification {
    public static Specification<CandidateSegmentQualification> hasCandidates(List<UUID> candidateIds) {
        return (root, query, criteriaBuilder) -> {
            if (candidateIds == null || candidateIds.isEmpty()) return null;

            /* select * from score scr */
            /* where scr.candidate_id in <candidateIds> */
            return root.get("candidate").get("id").in(candidateIds);
        };
    }

    public static Specification<CandidateSegmentQualification> hasSegment(UUID segmentId) {
        return (root, query, criteriaBuilder) -> {
            if (segmentId == null) return null;

            /* select * from candidateSegmentQualification csq */
            /* join csq.segment seg */
            Join<CandidateSegmentQualification, Segment> segment = root.join("segment", JoinType.INNER);
            /* where seg.id = <segmentId> */
            return criteriaBuilder.equal(segment.get("id"), segmentId);
        };
    }

    public static Specification<CandidateSegmentQualification> hasPageant(UUID pageantId) {
        return (root, query, criteriaBuilder) -> {
            if (pageantId == null) return null;

            /* select * from candidateSegmentQualification csq */
            /* join csq.segment seg */
            Join<CandidateSegmentQualification, Segment> segment = root.join("segment", JoinType.INNER);
            /* join seg.phase phs */
            Join<Segment, Phase> phase = segment.join("phase", JoinType.INNER);
            /* join phs.pageant pag */
            Join<Phase, Pageant> pageant = phase.join("pageant", JoinType.INNER);
            /* where pag.id = <pageantId> */
            return criteriaBuilder.equal(pageant.get("id"), pageantId);
        };
    }
}
