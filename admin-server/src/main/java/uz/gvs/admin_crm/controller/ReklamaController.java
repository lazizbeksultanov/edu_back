package uz.gvs.admin_crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.entity.User;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.ReklamaDto;
import uz.gvs.admin_crm.repository.ReklamaRepository;
import uz.gvs.admin_crm.security.CurrentUser;
import uz.gvs.admin_crm.service.ReklamaService;
import uz.gvs.admin_crm.utils.AppConstants;

@RestController
@RequestMapping("/api/reklama")
public class ReklamaController {
    @Autowired
    ReklamaService reklamaService;
    @Autowired
    ReklamaRepository reklamaRepository;

    @PostMapping
    public HttpEntity<?> addReklama(@RequestBody ReklamaDto reklamaDto) {
        ApiResponse apiResponse = reklamaService.addReklama(reklamaDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editReklama(@RequestBody ReklamaDto reklamaDto, @PathVariable Integer id) {
        ApiResponse apiResponse = reklamaService.editReklama(reklamaDto, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getReklamaList(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                        @CurrentUser User user) {
        ApiResponse reklamaList = reklamaService.getReklamaList(page, size, user);
        return ResponseEntity.status(reklamaList.isSuccess() ? 200 : 409).body(reklamaList);
    }

    @GetMapping("/select")
    public HttpEntity<?> getReklamaListForSelect() {
        ApiResponse reklamaList = reklamaService.getReklamaListForSelect();
        return ResponseEntity.status(reklamaList.isSuccess() ? 200 : 409).body(reklamaList);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteReklama(@PathVariable Integer id) {
        ApiResponse apiResponse = reklamaService.deleteReklama(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 204 : 409).body(apiResponse);
    }
}
