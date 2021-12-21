package uz.gvs.admin_crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.AppealDto;
import uz.gvs.admin_crm.service.AppealService;
import uz.gvs.admin_crm.utils.AppConstants;

import java.util.UUID;

@RestController
@RequestMapping("/api/appeal")
public class AppealController {
    @Autowired
    AppealService appealService;

    @PostMapping
    public HttpEntity<?> saveAppeal(@RequestBody AppealDto appealDto) {
        ApiResponse apiResponse = appealService.saveAppeal(appealDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/changeType/{id}")
    public HttpEntity<?> changeAppealStatus(@PathVariable UUID id, @RequestBody AppealDto appealDto) {
        ApiResponse apiResponse = appealService.changeStatus(id, appealDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getAppealById(@PathVariable UUID id) {
        ApiResponse apiResponse = appealService.getOneAppeal(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteAppeal(@PathVariable UUID id) {
        ApiResponse apiResponse = appealService.deleteAppeal(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getAppealList(
            @RequestParam(value = "enumType", defaultValue = "REQUEST") String enumType,
            @RequestParam(value = "typeId", defaultValue = "0") int typeId,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
    ) {
        ApiResponse apiResponse = appealService.getClientList(enumType, typeId, page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
