package uz.gvs.admin_crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.entity.User;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.RoomDto;
import uz.gvs.admin_crm.payload.TeacherDto;
import uz.gvs.admin_crm.security.CurrentUser;
import uz.gvs.admin_crm.service.TeacherService;
import uz.gvs.admin_crm.utils.AppConstants;

import java.util.UUID;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @PostMapping
    public HttpEntity<?> saveTeacher(@RequestBody TeacherDto teacherDto) {
        ApiResponse apiResponse = teacherService.saveTeacher(teacherDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


    @PutMapping("/{id}")
    public HttpEntity<?> save(@PathVariable UUID id, @RequestBody TeacherDto teacherDto) {
        ApiResponse apiResponse = teacherService.editTeacher(id, teacherDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getTeacherList(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                        @CurrentUser User user) {
        ApiResponse apiResponse = teacherService.getTeacherList(page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/select")
    public HttpEntity<?> getTeacherListForSelect() {
        ApiResponse apiResponse = teacherService.getTeacherListForSelect();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getStudent(@PathVariable UUID id) {
        ApiResponse apiResponse = teacherService.getTeacher(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/getGroups/{id}")
    public HttpEntity<?> getGroupsTeacher(@PathVariable UUID id) {
        ApiResponse apiResponse = teacherService.getGroupsTeacher(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteTeacher(@PathVariable UUID id) {
        ApiResponse apiResponse = teacherService.deleteTeacher(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 204 : 409).body(apiResponse);
    }
}
