package com.gdgocbu.tabulation.backend.utilities;

import com.gdgocbu.tabulation.backend.models.CandidateGender;
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
                            new CandidateTemp("Lea Jane","Torre", CandidateGender.FEMALE, 20),
                            new CandidateTemp("James","Sumayao", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    3,
                    "BUIPESR",
                    "Bicol University Institute of Physical Education, Sports, and Recreation",
                    List.of(
                            new CandidateTemp("Ziha Khrystell Shanice","Boncodin", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Piolo KR","Kawata", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    4,
                    "BUCSSP",
                    "Bicol University College of Social Sciences and Philosophy",
                    List.of(
                            new CandidateTemp("Rey-Anne","Oserin", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Lemuel Dave","Azaña", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    5,
                    "BUCBEM",
                    "Bicol University College of Business, Economics, and Management",
                    List.of(
                            new CandidateTemp("Cassandra Sofia","Tapia", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Lex Harvey","Revale", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    6,
                    "BUCL",
                    "Bicol University College of Law",
                    List.of(
                            new CandidateTemp("Julienne Zen","Vicente", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Austin Matthew","Olila", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    7,
                    "BUJMRIGD",
                    "Bicol University Jesse M Robredo Insitute of Governance and Development",
                    List.of(
                            new CandidateTemp("Annika","Maneja", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Kristian Carlo","Celadiña", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    8,
                    "BUIDeA",
                    "Bicol University Institute of Design and Architecture",
                    List.of(
                            new CandidateTemp("Risel","Narvaja", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Patrick Jed Niño","Alfante", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    9,
                    "BUCENG",
                    "Bicol University College of Engineering",
                    List.of(
                            new CandidateTemp("Sarah Mae","Gimena", CandidateGender.FEMALE, 20),
                            new CandidateTemp("George","Angeles", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    10,
                    "BUGuinobatan",
                    "Bicol University Guinobatan Campus",
                    List.of(
                            new CandidateTemp("Hannah","Bejison", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Rene Rey","Morota", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    11,
                    "BUTC",
                    "Bicol University Tabaco Campus",
                    List.of(
                            new CandidateTemp("Princess Mae","Perez", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Roberto","Cardiño", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    12,
                    "BUCE",
                    "Bicol University College of Education",
                    List.of(
                            new CandidateTemp("Mikylla Janelle","Imperial", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Lloyd Anthony","Bertes", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    13,
                    "BUGubat",
                    "Bicol University Gubat Campus",
                    List.of(
                            new CandidateTemp("Jesryl","Espantaleon", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Elvin","Ofalsa", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    14,
                    "BUCIT",
                    "Bicol University College of Industrial Technology",
                    List.of(
                            new CandidateTemp("Pearl Amirey","Espinili", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Aries Carl","Aycardo", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    15,
                    "BUCM",
                    "Bicol University College of Medicine",
                    List.of(
                            new CandidateTemp("Stephany Mae","Chi", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Jon","Perez", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    16,
                    "BUCN",
                    "Bicol University College of Nursing",
                    List.of(
                            new CandidateTemp("Kim Vianice Mae","Pilarte", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Jorick","Gustuir", CandidateGender.MALE, 20)
                    )
            ),
            new CollegeTemp(
                    17,
                    "BUCS",
                    "Bicol University College of Science",
                    List.of(
                            new CandidateTemp("Daisy","Camu", CandidateGender.FEMALE, 20),
                            new CandidateTemp("Austin","Fennis", CandidateGender.MALE, 20)
                    )
            )
    );

    public static final Map<String, CollegeTemp> collegesByCode =
            CollegeCandidateData.colleges.stream()
                    .collect(Collectors.toMap(CollegeTemp::getCode, c -> c));
}
