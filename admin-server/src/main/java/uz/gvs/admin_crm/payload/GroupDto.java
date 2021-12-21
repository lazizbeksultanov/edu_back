package uz.gvs.admin_crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.Course;
import uz.gvs.admin_crm.entity.CourseCategory;
import uz.gvs.admin_crm.entity.Room;
import uz.gvs.admin_crm.entity.Teacher;
import uz.gvs.admin_crm.entity.enums.GroupStatus;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupDto {
    private Integer id;
    private String name;
    private String description;
    private boolean active;
    private UUID teacherId;
    private String startTime;
    private String finishTime;
    private Integer courseId;
    private Integer roomId;
    private String startDate;
    private String finishDate;
    private Set<String> weekdays;
    private Teacher teacher;
    private Course course;
    private Room room;
    private GroupStatus groupStatus;

//    public CourseDto(Integer id, String name, String description, boolean active, double price, CourseCategory courseCategory){
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.active = active;
//        this.price = price;
//        this.courseCategory = courseCategory;
//    }
}
