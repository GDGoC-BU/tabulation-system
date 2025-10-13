package com.michaelcanonizado.backend.specifications;

import com.michaelcanonizado.backend.models.CandidateSegmentQualification;
import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Score;
import com.michaelcanonizado.backend.models.Segment;
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
}
