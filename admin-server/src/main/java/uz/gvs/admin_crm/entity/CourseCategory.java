package uz.gvs.admin_crm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.template.AbsNameEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CourseCategory extends AbsNameEntity {

    @ManyToOne
    private CourseCategory courseCategory;

    public CourseCategory(Integer id, String name, String description, boolean active, CourseCategory courseCategory) {
        super(id, name, description, active);
        this.courseCategory = courseCategory;
    }

    public CourseCategory(String name, String description, boolean active, CourseCategory courseCategory) {
        super(name, description, active);
        this.courseCategory = courseCategory;
    }
}
