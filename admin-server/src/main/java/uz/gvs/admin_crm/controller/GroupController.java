package uz.gvs.admin_crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.payload.AddGroupDto;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.GroupDto;
import uz.gvs.admin_crm.payload.SituationDto;
import uz.gvs.admin_crm.repository.GroupRepository;
import uz.gvs.admin_crm.service.ApiResponseService;
import uz.gvs.admin_crm.service.GroupService;
import uz.gvs.admin_crm.utils.AppConstants;

@RestController
@RequestMapping("api/group")
public class GroupController {
    @Autowired
    ApiResponseService apiResponseService;
    @Autowired
    GroupService groupService;
    @Autowired
    GroupRepository groupRepository;

    @PostMapping
    public HttpEntity<?> saveGroup(@RequestBody GroupDto groupDto) {
        ApiResponse apiResponse = groupService.saveGroup(groupDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    HttpEntity<?> getOneGroup(@PathVariable Integer id) {
        ApiResponse apiResponse = groupService.getOneGroup(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getGroupList(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        ApiResponse apiResponse = groupService.getGroupList(page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/select")
    public HttpEntity<?> getGroupForSelect() {
        ApiResponse apiResponse = groupService.getGroupsForSelect();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/addStudent")
    public HttpEntity<?> addStudentForGroup(@RequestBody AddGroupDto addGroupDto) {
        ApiResponse apiResponse = groupService.addStudentForGroup(addGroupDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editGroup(@PathVariable Integer id, @RequestBody GroupDto groupDto) {
        ApiResponse apiResponse = groupService.editGroup(groupDto, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @PatchMapping("/changeToArchiveStatus")
    public HttpEntity<?> changeToArchiveStatus(@RequestBody GroupDto groupDto) {
        ApiResponse apiResponse = groupService.changeToArchiveStatus(groupDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
    @PatchMapping("/changeToActiveStatus")
    public HttpEntity<?> changeToActiveStatus(@RequestBody GroupDto groupDto) {
        ApiResponse apiResponse = groupService.changeToActiveStatus(groupDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteGroup(@PathVariable Integer id) {
        try {
            groupRepository.deleteById(id);
            return ResponseEntity.status(204).body(apiResponseService.deleteResponse());
        } catch (Exception e) {
            return ResponseEntity.status(409).body(apiResponseService.tryErrorResponse());
        }
    }

}
