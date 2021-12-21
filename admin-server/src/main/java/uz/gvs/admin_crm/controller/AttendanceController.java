package uz.gvs.admin_crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.AttendanceDto;
import uz.gvs.admin_crm.repository.AttendanceRepository;
import uz.gvs.admin_crm.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    AttendanceService attendanceService;

    @PostMapping
    public HttpEntity<?> saveAttend(@RequestBody AttendanceDto attendanceDto) {
        ApiResponse apiResponse = attendanceService.saveAttendance(attendanceDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getAttendance(@PathVariable int id) {
        ApiResponse apiResponse = attendanceService.getAttendanceList(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

}
