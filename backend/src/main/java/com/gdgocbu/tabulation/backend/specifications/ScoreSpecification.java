package com.gdgocbu.tabulation.backend.specifications;

import com.gdgocbu.tabulation.backend.models.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
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

    public static Specification<Score> hasCandidates(List<UUID> candidateIds) {
        return (root, query, criteriaBuilder) -> {
            if (candidateIds == null || candidateIds.isEmpty()) return null;

            /* select * from score scr */
            /* where scr.candidate_id in <candidateIds> */
            return root.get("candidate").get("id").in(candidateIds);
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

    public static Specification<Score> hasCriteria(List<UUID> criterionIds) {
        return (root, query, criteriaBuilder) -> {
            if (criterionIds == null || criterionIds.isEmpty()) return null;

            /* select * from score scr */
            /* where scr.criterion_id in <criterionIds> */
            return root.get("criterion").get("id").in(criterionIds);
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

    public static Specification<Score> hasPageant(UUID pageantId) {
        return (root, query, criteriaBuilder) -> {
            if (pageantId == null) return null;

            /* select * from score scr */
            /* join scr.candidate cnd */
            Join<Score, Candidate> candidate = root.join("candidate", JoinType.INNER);
            /* join crt.pageant pgn */
            Join<Candidate, Pageant> pageant = candidate.join("pageant", JoinType.INNER);
            /* where pgn.id = <pageantId> */
            return criteriaBuilder.equal(pageant.get("id"), pageantId);
        };
    }
}
