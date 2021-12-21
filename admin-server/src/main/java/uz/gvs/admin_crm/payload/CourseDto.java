package uz.gvs.admin_crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.CourseCategory;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseDto {
    private Integer id;
    private String name;
    private String description;
    private boolean active;
    private double price;
    private Integer courseCategoryId;
    private CourseCategory courseCategory;

    public CourseDto(Integer id, String name, String description, boolean active, double price, CourseCategory courseCategory){
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.price = price;
        this.courseCategory = courseCategory;
    }
}
