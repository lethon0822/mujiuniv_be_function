package com.green.muziuniv_be_notuser.openfeign.department;

import com.green.muziuniv_be_notuser.configuration.FeignConfiguration;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.app.shared.department.model.DepartmentRes;
import com.green.muziuniv_be_notuser.openfeign.department.model.DepartmentHeadNameRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "${constants.open-feign.user}", contextId = "departmentClient",configuration = FeignConfiguration.class)
public interface DepartmentClient {
    @GetMapping("/api/dept/list")
    ResultResponse<List<DepartmentRes>> getDepartments();

    // deptId로 해당 학과의 교수 리스트 가져옴
    @GetMapping("/api/dept/head")
    ResultResponse<List<DepartmentHeadNameRes>> findDeptHeadList(@RequestParam("dept_id") Long deptId);
}
