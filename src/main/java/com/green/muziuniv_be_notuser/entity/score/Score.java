package com.green.muziuniv_be_notuser.entity.score;

import com.green.muziuniv_be_notuser.entity.UpdatedAt;
import com.green.muziuniv_be_notuser.entity.enrollment.Enrollment;
import jakarta.persistence.*;


@Entity
public class Score extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scoreId;

    @OneToOne
    @JoinColumn(name = "enrollment_id", nullable = false, unique = true)
    private Enrollment enrollment;

    @Column(name = "mid_score")
    private int midScore;

    @Column(name = "fin_score")
    private int finScore;

    @Column(length = 5)
    private String rank;

}
