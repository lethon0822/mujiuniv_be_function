package com.green.muziuniv_be_notuser.entity.course;

import com.green.muziuniv_be_notuser.entity.CreatedAt;
import com.green.muziuniv_be_notuser.entity.UserId;
import com.green.muziuniv_be_notuser.entity.semester.Semester;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(
        name = "course",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_course_code_semester_id", columnNames = {"course_code", "semester_id"})
        }
)
public class Course extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false)
    private UserId userId;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semesterId;

    @Column(nullable = false, length = 20)
    private String classroom;

    @Column(nullable = false, length = 5)
    private String type;

    @Column(nullable = false, length = 50)
    private String time;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private Integer remStd = 0;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "처리중";

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false)
    private Long credit;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String weekPlan;

    @Column(length = 100)
    private String textBook;

    @Column(nullable = false, length = 225)
    private String goal;

    @Column(nullable = false)
    private Integer maxStd;

    @Column(nullable = false)
    private Integer grade;

    @Column(unique = true, nullable = false, length = 20)
    private String courseCode;






}
