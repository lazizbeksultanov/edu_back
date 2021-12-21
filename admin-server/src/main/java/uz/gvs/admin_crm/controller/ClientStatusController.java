package uz.gvs.admin_crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.ClientStatusDto;
import uz.gvs.admin_crm.service.ClientStatusService;

@RestController
@RequestMapping("/api/clientStatus")
public class ClientStatusController {
    @Autowired
    ClientStatusService clientStatusService;

    @PostMapping
    public HttpEntity<?> saveClientStatus(@RequestBody ClientStatusDto clientStatusDto) {
        ApiResponse apiResponse = clientStatusService.saveClientStatus(clientStatusDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editClientStatus(@PathVariable Integer id, @RequestBody ClientStatusDto clientStatusDto) {
        ApiResponse apiResponse = clientStatusService.editClientStatus(id, clientStatusDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/list")
    public HttpEntity<?> getClientStatusList(@RequestParam(value = "type", defaultValue = "all") String type) {
        ApiResponse apiResponse = clientStatusService.getClientStatus(type);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteClientStatus(@PathVariable Integer id) {
        ApiResponse apiResponse = clientStatusService.deleteClientStatus(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 204 : 409).body(apiResponse);
    }
}
