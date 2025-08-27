package com.michaelcanonizado.backend.specifications;

import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Score;
import com.michaelcanonizado.backend.models.Segment;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ScoreSpecification {
    public static Specification<Score> hasJudge(UUID judgeId) {
        return (root, query, criteriaBuilder) -> {
            if (judgeId == null)  return null;

            /* select * from score scr */
            /* scr.judge_id = <judgeId> */
            return criteriaBuilder.equal(root.get("judge").get("id"), judgeId);
        };
    }

    public static Specification<Score> hasCandidate(UUID candidateId) {
        return (root, query, criteriaBuilder) -> {
            if (candidateId == null) return null;

            /* select * from score scr */
            /* scr.candidate_id = <candidateId> */
            return criteriaBuilder.equal(root.get("candidate").get("id"), candidateId);
        };
    }

    public static Specification<Score> hasCriterion(UUID criterionId) {
        return (root, query, criteriaBuilder) -> {
            if (criterionId == null) return null;

            /* select * from score scr */
            /* scr.criterion_id = <criterionId> */
            return criteriaBuilder.equal(root.get("criterion").get("id"), criterionId);
        };
    }

    public static Specification<Score> hasSegment(UUID segmentId) {
        return (root, query, criteriaBuilder) -> {
            if (segmentId == null) return null;

            /* select * from score scr */
            /* join scr.criterion crt */
            Join<Score, Criterion> criterion = root.join("criterion", JoinType.INNER);
            /* join crt.segment seg */
            Join<Criterion, Segment> segment = criterion.join("segment", JoinType.INNER);
            /* where seg.id = <segmentId> */
            return criteriaBuilder.equal(segment.get("id"), segmentId);
        };
    }
}
