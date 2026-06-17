package com.gdgocbu.tabulation.backend.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PhaseSegmentCriteriaData {
    @AllArgsConstructor
    @Getter
    public static class CriterionTemp {
        private String name;
        private int maxScore;
    }

    @AllArgsConstructor
    @Getter
    public static class SegmentTemp {
        private String name;
        private int sequence;
        private List<CriterionTemp> criteria;
    }

    @AllArgsConstructor
    @Getter
    public static class PhaseTemp {
        private String name;
        private int sequence;
        private List<SegmentTemp> segments;
    }

    public static List<PhaseTemp> phases = Arrays.asList(
            new PhaseTemp(
                    "Closed Door Interview",
                    1,
                    List.of(
                            new SegmentTemp(
                                    "Swimwear",
                                    1,
                                    List.of(
                                            new CriterionTemp("Presence & Projection", 15),
                                            new CriterionTemp("Poise", 10),
                                            new CriterionTemp("Physical Well-being & Vitality", 5)
                                    )
                            ),
                            new SegmentTemp(
                                    "Formal Attire",
                                    2,
                                    List.of(
                                            new CriterionTemp("Attire, Elegance, & Grace", 20),
                                            new CriterionTemp("Stage Presence", 10)
                                    )
                            ),
                            new SegmentTemp(
                                    "Question & Answer",
                                    3,
                                    List.of(
                                            new CriterionTemp("Intelligence", 20),
                                            new CriterionTemp("Poise & Personality", 20)
                                    )
                            )
                    )
            ),
            new PhaseTemp(
                    "Coronation Night",
                    2,
                    List.of(
                            new SegmentTemp(
                                    "Swimwear",
                                    1,
                                    List.of(
                                            new CriterionTemp("Presence & Projection", 25),
                                            new CriterionTemp("Poise", 20),
                                            new CriterionTemp("Physical Well-being & Vitality", 5)
                                    )
                            ),
                            new SegmentTemp(
                                    "Formal Attire",
                                    2,
                                    List.of(
                                            new CriterionTemp("Attire, Elegance, & Grace", 30),
                                            new CriterionTemp("Stage Presence", 20)
                                    )
                            ),
                            new SegmentTemp(
                                    "Semi-Final Question & Answer",
                                    3,
                                    List.of(
                                            new CriterionTemp("Intelligence", 50),
                                            new CriterionTemp("Alignment with Advocacy or Purpose", 25),
                                            new CriterionTemp("Poise & Personality", 25)
                                    )
                            ),
                            new SegmentTemp(
                                    "Final Question & Answer",
                                    4,
                                    List.of(
                                            new CriterionTemp("Intelligence", 50),
                                            new CriterionTemp("Relevance", 25),
                                            new CriterionTemp("Poise & Personality", 25)
                                    )
                            )
                    )
            )
    );

    public static Map<String,PhaseTemp> phaseByName =
            phases.stream().collect(Collectors.toMap(PhaseTemp::getName, p -> p));

    public static Map<String, Map<String, SegmentTemp>> segmentByPhaseNameAndSegmentName =
            phases.stream().collect(
                Collectors.toMap(
                    PhaseTemp::getName,
                    phase -> phase.getSegments().stream().collect(
                        Collectors.toMap(
                            SegmentTemp::getName,
                            s -> s
                        )
                    )
                )
            );
}
