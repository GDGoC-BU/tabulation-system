package com.michaelcanonizado.backend.specifications;

import com.michaelcanonizado.backend.models.Score;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ScoreSpecification {
    public static Specification<Score> hasJudge(UUID judgeId) {
        return (root, query, criteriaBuilder) -> {
            if (judgeId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("judge").get("id"), judgeId);
        };
    }

    public static Specification<Score> hasCandidate(UUID candidateId) {
        return (root, query, criteriaBuilder) -> {
            if (candidateId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("candidate").get("id"), candidateId);
        };
    }

    public static Specification<Score> hasCriterion(UUID criterionId) {
        return (root, query, criteriaBuilder) -> {
            if (criterionId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("criterion").get("id"), criterionId);
        };
    }
}
