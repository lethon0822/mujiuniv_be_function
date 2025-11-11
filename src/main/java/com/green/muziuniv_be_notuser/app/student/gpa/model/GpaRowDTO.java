package com.green.muziuniv_be_notuser.app.student.gpa.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GpaRowDTO {
    private Long semesterId;
    private Integer year;
    private Integer semester;
    private Integer credit;
    private String type;
    private String rank;
}
