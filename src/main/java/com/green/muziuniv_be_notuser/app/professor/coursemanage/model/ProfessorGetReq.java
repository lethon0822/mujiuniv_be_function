package com.green.muziuniv_be_notuser.app.professor.coursemanage.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProfessorGetReq {
    private Long userId;
    private Integer year;
    private Integer semester;
    private Integer sid;




}
