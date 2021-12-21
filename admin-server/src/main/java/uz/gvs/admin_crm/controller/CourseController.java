package uz.gvs.admin_crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gvs.admin_crm.entity.User;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.CourseCategoryDto;
import uz.gvs.admin_crm.payload.CourseDto;
import uz.gvs.admin_crm.repository.CourseCategoryRepository;
import uz.gvs.admin_crm.repository.CourseRepository;
import uz.gvs.admin_crm.security.CurrentUser;
import uz.gvs.admin_crm.service.ApiResponseService;
import uz.gvs.admin_crm.service.CourseCategoryService;
import uz.gvs.admin_crm.service.CourseService;
import uz.gvs.admin_crm.utils.AppConstants;

@RestController
@RequestMapping("api/course")
public class CourseController {
    @Autowired
    ApiResponseService apiResponseService;
    @Autowired
    CourseService courseService;
    @Autowired
    CourseRepository courseRepository;

    @PostMapping
    public HttpEntity<?> saveCourse(@RequestBody CourseDto courseDto) {
        ApiResponse apiResponse = courseService.saveCourse(courseDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/{id}")
    HttpEntity<?> getOneCourse(@PathVariable Integer id) {
        ApiResponse apiResponse = courseService.getOneCourse(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/select")
    HttpEntity<?> getCourseListForSelect() {
        ApiResponse apiResponse = courseService.getCourseList();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getCourseList(
            @RequestParam(value = "categoryId", defaultValue = "0") int categoryId,
            @CurrentUser User user) {
        ApiResponse apiResponse = courseService.getCourseList(categoryId, user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editCourse(@PathVariable Integer id, @RequestBody CourseDto courseDto) {
        ApiResponse apiResponse = courseService.editCourse(courseDto, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable Integer id) {
        try {
            courseRepository.deleteById(id);
            return ResponseEntity.status(204).body(apiResponseService.deleteResponse());
        } catch (Exception e) {
            return ResponseEntity.status(409).body(apiResponseService.tryErrorResponse());
        }
    }
}
//
//