package uz.gvs.admin_crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.PayTypeDto;
import uz.gvs.admin_crm.repository.PayTypeRepository;
import uz.gvs.admin_crm.service.PayTypeService;

@RestController
@RequestMapping("/api/payType")
public class PayTypeController {

    @Autowired
    PayTypeRepository payTypeRepository;

    @Autowired
    PayTypeService payTypeService;

    @PostMapping
    public HttpEntity<?> save(@RequestBody PayTypeDto payTypeDto) {
        ApiResponse apiResponse = payTypeService.save(payTypeDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> save(@PathVariable Integer id, @RequestBody PayTypeDto payTypeDto) {
        payTypeDto.setId(id);
        ApiResponse apiResponse = payTypeService.save(payTypeDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getList() {
        ApiResponse apiResponse = payTypeService.getPayTypeList();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getPayType(@PathVariable Integer id){
        ApiResponse apiResponse = payTypeService.getPayType(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deletePayType(@PathVariable Integer id){
        ApiResponse apiResponse = payTypeService.deletePayType(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 204 : 409).body(apiResponse);
    }

}
