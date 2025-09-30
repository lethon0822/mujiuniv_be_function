package com.green.muziuniv_be_notuser.entity.classroom;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classroomId;

    @Column(nullable = false, length = 20)
    private String roomName;

    @Column(nullable = false, length = 20)
    private int roomNumber;
}
