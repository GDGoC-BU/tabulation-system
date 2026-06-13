package com.gdgocbu.tabulation.backend.specifications;

import com.gdgocbu.tabulation.backend.models.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CriterionSpecification {
    public static Specification<Criterion> hasPageant(UUID pageantId) {
        return (root, query, criteriaBuilder) -> {
            if (pageantId == null) return null;

            /* select * from criterion crt */
            /* join crt.segment seg */
            Join<Criterion, Segment> segment = root.join("segment", JoinType.INNER);
            /* join seg.phase phs */
            Join<Segment, Phase> phase = segment.join("phase", JoinType.INNER);
            /* join phase.pageant pgn */
            Join<Phase, Pageant> pageant = phase.join("pageant", JoinType.INNER);
            /* where pgn.id = <pageantId> */
            return criteriaBuilder.equal(pageant.get("id"), pageantId);
        };
    }
}
