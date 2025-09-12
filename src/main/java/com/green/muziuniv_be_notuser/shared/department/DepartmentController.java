package com.green.muziuniv_be_notuser.shared.department;

import com.green.muziuniv_be_notuser.openfeign.department.DepartmentClient;
import com.green.muziuniv_be_notuser.shared.department.model.DepartmentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentClient departmentClient;

    // 개설 학과 목록 조회 ( 서치필터바에 띄울 용도 )
    @GetMapping
    public List<DepartmentRes> getDepartments() {
        return departmentClient.getDepartments().getResult();
    }
}
