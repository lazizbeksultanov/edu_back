package uz.gvs.admin_crm.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.gvs.admin_crm.service.AttachmentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    @PostMapping("/upload")
    public HttpEntity<?> uploadFile(MultipartHttpServletRequest request) {
        UUID uuid = attachmentService.uploadFile(request);
        return ResponseEntity.ok(uuid);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getFile(@PathVariable UUID id) {
        return attachmentService.getFile(id);
    }
}
