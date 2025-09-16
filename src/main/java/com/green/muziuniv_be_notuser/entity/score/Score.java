package com.green.muziuniv_be_notuser.entity.score;

import com.green.muziuniv_be_notuser.entity.UpdatedAt;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "score")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Score extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false, unique = true)
    private Enrollment enrollment;

    @Column(name = "mid_score")
    private int midScore;

    @Column(name = "fin_score")
    private int finScore;
    @Column(name = "attendance_score")
    private int attendanceScore;

    @Column(name = "other_score")
    private int otherScore;

    @Column(length = 5)
    private String rank;

    @Column
    private int grade;  // 학점 값 (예: 1=Pass, 2=Fail 등)
}

