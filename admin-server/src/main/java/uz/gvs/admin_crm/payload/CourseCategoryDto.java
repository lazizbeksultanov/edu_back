package uz.gvs.admin_crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.CourseCategory;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseCategoryDto {
    private Integer id;
    private String name;
    private String description;
    private boolean active;
    private Integer courseCategoryId;
    private boolean isParent;

    public CourseCategoryDto(Integer id, String name, String description, boolean active, Integer courseCategoryId, CourseCategory courseCategory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.courseCategoryId = courseCategoryId;
        this.courseCategory = courseCategory;
    }

    public CourseCategoryDto(Integer id, String name, String description, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
    }

    private CourseCategory courseCategory;
}
