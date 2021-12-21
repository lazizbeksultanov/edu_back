package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.gvs.admin_crm.entity.Attachment;
import uz.gvs.admin_crm.entity.AttachmentContent;
import uz.gvs.admin_crm.repository.AttachmentContentRepository;
import uz.gvs.admin_crm.repository.AttachmentRepository;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

@Service
public class AttachmentService {
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    public UUID uploadFile(MultipartHttpServletRequest request) {
        try {
            Iterator<String> fileNames = request.getFileNames();
            /////while(fileNames.hasNext)   agar ishlamasa while ni ichiga olib qo'yish kerak
            MultipartFile file = request.getFile(fileNames.next());
            //asert bu test qilish uchun
            assert file != null;
            Attachment attachment = new Attachment(file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize());
            Attachment savedAttachment = attachmentRepository.save(attachment);

            AttachmentContent attachmentContent = new AttachmentContent(savedAttachment, file.getBytes());

            attachmentContentRepository.save(attachmentContent);
            return savedAttachment.getId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public HttpEntity<?> getFile(UUID id){
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getAttachment"));
        AttachmentContent attachmentContent = attachmentContentRepository.findByAttachmentId(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename:\""+attachment.getName()+"\"")
                .body(attachmentContent.getContent());

    }
}
