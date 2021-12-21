package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.Course;
import uz.gvs.admin_crm.entity.CourseCategory;
import uz.gvs.admin_crm.entity.Region;
import uz.gvs.admin_crm.entity.User;
import uz.gvs.admin_crm.payload.ApiResponse;
import uz.gvs.admin_crm.payload.CourseCategoryDto;
import uz.gvs.admin_crm.payload.PageableDto;
import uz.gvs.admin_crm.repository.CourseCategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseCategoryService {
    @Autowired
    ApiResponseService apiResponseService;
    @Autowired
    CourseCategoryRepository courseCategoryRepository;


    public ApiResponse saveCourseCategory(CourseCategoryDto courseCategoryDto) {
        if (courseCategoryDto.getCourseCategoryId() != null) {
            CourseCategory parentCategory = courseCategoryRepository.findById(courseCategoryDto.getCourseCategoryId()).orElseThrow(() -> new ResourceNotFoundException("get courseCategory"));
            boolean exists = courseCategoryRepository.existsByNameEqualsIgnoreCaseAndCourseCategoryId(courseCategoryDto.getName(), parentCategory.getId());
            if (exists) {
                return apiResponseService.existResponse();
            }
            makeCourseCategory(courseCategoryDto, parentCategory);
            return apiResponseService.saveResponse();
        }
        if (courseCategoryRepository.existsByNameEqualsIgnoreCaseAndCourseCategory(courseCategoryDto.getName(), null)) {
            return apiResponseService.existResponse();
        }
        makeCourseCategory(courseCategoryDto, null);
        return apiResponseService.saveResponse();
    }

    public ApiResponse getOneCourseCategory(Integer id) {
        try {
            Optional<CourseCategory> byId = courseCategoryRepository.findById(id);
            if (!byId.isPresent()) {
                return apiResponseService.notFoundResponse();
            }
            return apiResponseService.getResponse(makeCourseForGet(byId.get()));
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    private CourseCategoryDto makeCourseForGet(CourseCategory courseCategory) {
        return new CourseCategoryDto(
                courseCategory.getId(),
                courseCategory.getName(),
                courseCategory.getDescription(),
                courseCategory.isActive(),
                courseCategory.getCourseCategory() != null ? courseCategory.getCourseCategory().getId() : null,
                courseCategory.getCourseCategory()
        );
    }

    public CourseCategory makeCourseCategory(CourseCategoryDto courseCategoryDto, CourseCategory parent) {
        return courseCategoryRepository.save(
                new CourseCategory(
                        courseCategoryDto.getName(),
                        courseCategoryDto.getDescription(),
                        courseCategoryDto.isActive(),
                        parent
                ));
    }

    public ApiResponse getCourseCategoryList(int id, User user) {
        try {
            List<CourseCategory> all = null;
            if (id > 0) {
                all = courseCategoryRepository.findAllByCourseCategory_Id(id);
            } else {
                all = courseCategoryRepository.findAllByCourseCategoryIsNull();
            }
            return apiResponseService.getResponse(all.stream().map(this::makeCourseForGet).collect(Collectors.toList()));
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }

    public ApiResponse editCourseCategory(CourseCategoryDto courseCategoryDto, Integer id) {
        try {
            Optional<CourseCategory> optional = courseCategoryRepository.findById(id);
            if (optional.isPresent()) {
                CourseCategory courseCategory = optional.get();
                if (courseCategoryDto.getCourseCategoryId() != null) {
                    if (courseCategory.getCourseCategory() != null && !courseCategoryDto.getCourseCategoryId().equals(courseCategory.getCourseCategory().getId())) {
                        if (courseCategoryRepository.existsByNameEqualsIgnoreCaseAndIdNotAndCourseCategoryId(courseCategoryDto.getName(), id, courseCategoryDto.getCourseCategoryId()))
                            return apiResponseService.existResponse();
                    }
                    courseCategory.setCourseCategory(courseCategoryRepository.findById(courseCategoryDto.getCourseCategoryId()).orElseThrow(() -> new ResourceNotFoundException("get CourseCategory")));
                    if (id.equals(courseCategoryDto.getCourseCategoryId())) {
                        return apiResponseService.errorResponse();
                    }
                } else {
                    if (courseCategoryRepository.existsByNameEqualsIgnoreCaseAndCourseCategoryAndIdNot(courseCategoryDto.getName(), null, id))
                        return apiResponseService.existResponse();
                    courseCategory.setCourseCategory(null);
                }
                courseCategory.setName(courseCategoryDto.getName());
                courseCategory.setDescription(courseCategoryDto.getDescription());
                courseCategory.setActive(courseCategoryDto.isActive());
                courseCategoryRepository.save(courseCategory);
                return apiResponseService.updatedResponse();
            }
            return apiResponseService.notFoundResponse();
        } catch (Exception e) {
            return apiResponseService.tryErrorResponse();
        }
    }
}
