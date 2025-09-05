package com.green.muziuniv_be_notuser.entity.semester;


import jakarta.persistence.*;
import lombok.*;

import java.time.Year;

@Entity
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table( // 이건 유니크 걸기 작업
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"year", "semester"}
                )
        }
)
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long semesterId;

    @Column(nullable = false, columnDefinition = "YEAR")
    private Year year;

    @Column(nullable = false)
    private Integer semester;




}
