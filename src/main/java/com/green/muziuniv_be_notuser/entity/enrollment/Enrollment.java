package com.green.muziuniv_be_notuser.entity.enrollment;

import com.green.muziuniv_be_notuser.entity.CreatedAt;
import com.green.muziuniv_be_notuser.entity.course.Course;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "enrollment",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"course_id", "user_id"}
                )
        }
)
public class Enrollment extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // Embedded 제거 → 그냥 Long으로
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(length = 255)
    private String review;

    @Column(name = "ev_score")
    private int evScore;

    @Column(nullable = false)
    private String status = "수강중";


}
