package uz.gvs.admin_crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.entity.TeacherSalary;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.ClientDto;
import uz.gvs.admin_crm.payload.TeacherSalaryDto;
import uz.gvs.admin_crm.repository.TeacherSalaryRepository;
import uz.gvs.admin_crm.service.ApiResponseService;
import uz.gvs.admin_crm.service.TeacherSalaryService;
import uz.gvs.admin_crm.utils.AppConstants;

import java.util.UUID;

@RestController
@RequestMapping("/api/teacherSalary")
public class TeacherSalaryController {
    @Autowired
    TeacherSalaryService service;
    @Autowired
    TeacherSalaryRepository teacherSalaryRepository;
    @Autowired
    ApiResponseService apiResponseService;


    @PostMapping
    public HttpEntity<?> minusAmount(@RequestBody TeacherSalaryDto teacherSalaryDto) {
        ApiResponse apiResponse = service.minusAmount(teacherSalaryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getSalaries(@PathVariable UUID id, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                   @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size){
        ApiResponse apiResponse = service.getSalaries(id, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    @GetMapping
    public HttpEntity<?> getAllSalaries(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                   @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size){
        ApiResponse apiResponse = service.getAllSalaries(page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editSalary(@PathVariable UUID id, @RequestBody TeacherSalaryDto teacherSalaryDto){
        ApiResponse apiResponse = service.editSalary(id, teacherSalaryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSalary(@PathVariable UUID id) {
        try {
            teacherSalaryRepository.deleteById(id);
            return ResponseEntity.status(204).body(apiResponseService.deleteResponse());
        } catch (Exception e) {
            return ResponseEntity.status(409).body(apiResponseService.tryErrorResponse());
        }
    }
}
