package com.green.muziuniv_be_notuser.openfeign.department;

import com.green.muziuniv_be_notuser.configuration.FeignConfiguration;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import com.green.muziuniv_be_notuser.app.shared.department.model.DepartmentRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "${constants.open-feign.user}", contextId = "departmentClient",configuration = FeignConfiguration.class)
public interface DepartmentClient {
    @GetMapping("/api/dept/list")
    ResultResponse<List<DepartmentRes>> getDepartments();
}
