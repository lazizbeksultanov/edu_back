package uz.gvs.admin_crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.ToplamDto;
import uz.gvs.admin_crm.service.ToplamService;
import uz.gvs.admin_crm.utils.AppConstants;

@RestController
@RequestMapping("/api/toplam")
public class ToplamController {
    @Autowired
    ToplamService toplamService;

    @PostMapping
    public HttpEntity<?> saveToplam(@RequestBody ToplamDto toplamDto) {
        ApiResponse apiResponse = toplamService.saveToplam(toplamDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editToplam(@PathVariable int id, @RequestBody ToplamDto toplamDto) {
        ApiResponse apiResponse = toplamService.editToplam(id, toplamDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getToplam(@PathVariable int id) {
        ApiResponse apiResponse = toplamService.getOneToplam(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getToplamList(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        ApiResponse apiResponse = toplamService.getToplamList(page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/select")
    public HttpEntity<?> getToplamListForSelect() {
        ApiResponse apiResponse = toplamService.getToplamListForSelect();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteToplam(int id) {
        ApiResponse apiResponse = toplamService.deleteToplam(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 204 : 409).body(apiResponse);
    }

}
