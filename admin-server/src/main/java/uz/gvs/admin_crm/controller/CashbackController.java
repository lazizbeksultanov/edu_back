package uz.gvs.admin_crm.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.entity.User;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.CashbackDto;
import uz.gvs.admin_crm.repository.CashbackRepository;
import uz.gvs.admin_crm.security.CurrentUser;
import uz.gvs.admin_crm.service.ApiResponseService;
import uz.gvs.admin_crm.service.CashbackService;
import uz.gvs.admin_crm.utils.AppConstants;

@RestController
@RequestMapping("/api/cashback")
public class CashbackController {
    @Autowired
    CashbackService cashbackService;
    @Autowired
    CashbackRepository cashbackRepository;
    @Autowired
    ApiResponseService apiResponseService;

    @PostMapping
    public HttpEntity<?> saveCashback(@RequestBody CashbackDto cashbackDto) {
        ApiResponse apiResponse = cashbackService.saveCashback(cashbackDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editCashback(@PathVariable Integer id, @RequestBody CashbackDto cashbackDto) {
        ApiResponse apiResponse = cashbackService.editCashback(id, cashbackDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getCashbackList(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                         @CurrentUser User user) {
        ApiResponse apiResponse = cashbackService.getCashbackList(page, size, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getCashback(@PathVariable Integer id) {
        ApiResponse apiResponse = cashbackService.getCashback(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCashback(@PathVariable Integer id) {
        try {
            cashbackRepository.deleteById(id);
            return ResponseEntity.status(204).body(apiResponseService.deleteResponse());
        } catch (Exception e) {
            return ResponseEntity.status(409).body(apiResponseService.tryErrorResponse());
        }
    }

}
