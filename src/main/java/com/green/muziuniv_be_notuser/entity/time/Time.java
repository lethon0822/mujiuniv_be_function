package com.green.muziuniv_be_notuser.entity.time;

import com.green.muziuniv_be_notuser.entity.course.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@Table(name = "Time")
@EntityListeners(AuditingEntityListener.class)
public class Time  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT
    @Column(name = "id", nullable = false )
    private Integer id;

    @Column(name = "time", length =5)
    private String time;


    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false )
    private Course courseId;

}
