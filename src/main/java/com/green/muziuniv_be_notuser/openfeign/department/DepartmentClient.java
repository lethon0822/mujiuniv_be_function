package com.green.muziuniv_be_notuser.openfeign.department;

import com.green.muziuniv_be_notuser.common.util.FeignConfiguration;
import com.green.muziuniv_be_notuser.shared.department.model.DepartmentRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "${constants.open-feign.user}", contextId = "departmentClient",configuration = FeignConfiguration.class)
public interface DepartmentClient {
    @GetMapping("/api/staff/department")
    List<DepartmentRes> getDepartments();
}
