package com.gdgocbu.tabulation.backend.specifications;

import com.gdgocbu.tabulation.backend.models.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class SegmentSpecification {
    public static Specification<Segment> hasPageant(UUID pageantId) {
        return (root, query, criteriaBuilder) -> {
            if (pageantId == null) return null;

            /* select * from segment seg */
            /* join seg.phase phs */
            Join<Segment, Phase> phase = root.join("phase", JoinType.INNER);
            /* join phs.pageant pgn */
            Join<Phase, Pageant> pageant = phase.join("pageant", JoinType.INNER);
            /* where pgn.id = <pageantId> */
            return criteriaBuilder.equal(pageant.get("id"), pageantId);
        };
    }
}
