package com.green.muziuniv_be_notuser.shared.semester.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SemesterCreateReq {
    @NotNull
    @Min(2000) @Max(2100)
    private Integer year;
    @NotNull @Min(1) @Max(4)
    private Integer semester;
}