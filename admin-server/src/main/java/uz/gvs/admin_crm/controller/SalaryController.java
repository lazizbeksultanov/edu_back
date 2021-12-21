package uz.gvs.admin_crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.TeacherDto;
import uz.gvs.admin_crm.service.SalaryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {
    @Autowired
    SalaryService salaryService;

    @PostMapping("/{id}")
    public HttpEntity<?> saveSalary( @PathVariable UUID id,@RequestBody TeacherDto teacherDto) {
        ApiResponse apiResponse = salaryService.saveSalary(id,teacherDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }
}
