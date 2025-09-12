package com.green.muziuniv_be_notuser.entity.attendance;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class AttendanceIds implements Serializable {
    @Column(name = "attend_date", nullable = false)
    private LocalDate attendDate;
    @Column(name = "enrollment_id", nullable = false )
    private int enrollmentId;
}
