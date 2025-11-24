package com.michaelcanonizado.backend.utilities;

import com.michaelcanonizado.backend.models.CandidateGender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CollegeCandidateData {
    @AllArgsConstructor
    @Getter
    public static class CandidateTemp {
        private String firstName;
        private String lastName;
        private CandidateGender gender;
        private int age;
    }

    @AllArgsConstructor
    @Getter
    public static class CollegeTemp {
        private int number;
        private String code;
        private String name;
        private List<CandidateTemp> candidates;
    }

    public static List<CollegeTemp> colleges = Arrays.asList(
            new CollegeTemp(
                    1,
                    "BUCAL",
                    "Bicol University College of Arts and Letters",
                    List.of(
                            new CandidateTemp("Isha Deianira","Celestino", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Ken Lawrence","Ante", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    2,
                    "BUPC",
                    "Bicol University Polangui Campus",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    3,
                    "BUIPESR",
                    "Bicol University Institute of Physical Education, Sports, and Recreation",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    4,
                    "BUCSSP",
                    "Bicol University College of Social Sciences and Philosophy",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    5,
                    "BUCBEM",
                    "Bicol University College of Business, Economics, and Management",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    6,
                    "BUCL",
                    "Bicol University College of Law",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    7,
                    "BUJMRIGD",
                    "Bicol University Jesse M Robredo Insitute of Governance and Development",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    8,
                    "BUIDeA",
                    "Bicol University Institute of Design and Architecture",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    9,
                    "BUCENG",
                    "Bicol University College of Engineering",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    10,
                    "BUGuinobatan",
                    "Bicol University Guinobatan Campus",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    11,
                    "BUTC",
                    "Bicol University Tabaco Campus",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    12,
                    "BUCE",
                    "Bicol University College of Education",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    13,
                    "BUGubat",
                    "Bicol University Gubat Campus",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    14,
                    "BUCIT",
                    "Bicol University College of Industrial Technology",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    15,
                    "BUCM",
                    "Bicol University College of Medicine",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    16,
                    "BUCN",
                    "Bicol University College of Nursing",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    17,
                    "BUCS",
                    "Bicol University College of Science",
                    List.of(
                            new CandidateTemp("","", CandidateGender.FEMALE, 20),
                            new CandidateTemp("","", CandidateGender.MALE, 20)
                    )
            )
    );

    public static final Map<String, CollegeTemp> collegesByCode =
            CollegeCandidateData.colleges.stream()
                    .collect(Collectors.toMap(CollegeTemp::getCode, c -> c));
}
