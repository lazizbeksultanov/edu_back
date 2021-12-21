package uz.gvs.admin_crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.RoomDto;
import uz.gvs.admin_crm.repository.RoomRepository;
import uz.gvs.admin_crm.service.RoomService;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    RoomService roomService;

    @PostMapping
    public HttpEntity<?> save(@RequestBody RoomDto roomDto) {
        ApiResponse apiResponse = roomService.save(roomDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> save(@PathVariable Integer id, @RequestBody RoomDto roomDto) {
        roomDto.setId(id);
        ApiResponse apiResponse = roomService.save(roomDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getList() {
        ApiResponse apiResponse = roomService.getRoomList();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getRoom(@PathVariable Integer id){
        ApiResponse apiResponse = roomService.getRoom(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteRoom(@PathVariable Integer id){
        ApiResponse apiResponse = roomService.deleteRoom(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 204 : 409).body(apiResponse);
    }

}
