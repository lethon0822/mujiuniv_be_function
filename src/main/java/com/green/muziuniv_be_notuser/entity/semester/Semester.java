package com.green.muziuniv_be_notuser.entity.semester;


import com.green.muziuniv_be_notuser.entity.CreatedAt;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.Year;

@Entity
@Table(
        name = "semester",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_semester_year_semester", columnNames = {"year", "semester"})
        }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Long semesterId;

    @Column(nullable = false)
    private Integer year;               // 예: 2025

    @Column(nullable = false)
    private Integer semester; // 예: 1, 2 (또는 계절학기 등 규칙에 맞게)
}
