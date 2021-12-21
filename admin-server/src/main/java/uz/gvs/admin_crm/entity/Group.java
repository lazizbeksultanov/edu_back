package uz.gvs.admin_crm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.enums.GroupStatus;
import uz.gvs.admin_crm.entity.template.AbsNameEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "groups")
public class Group extends AbsNameEntity {
    @ManyToOne
    private Teacher teacher;
    private String startTime;
    private String finishTime;
    @ManyToOne
    private Course course;
    @ManyToOne
    private Room room;
    private Date finishDate;
    private Date startDate;
    @Enumerated(EnumType.STRING)
    private GroupStatus groupStatus;
    private boolean active;

    @ManyToMany
    @JoinTable(name = "group_weekdays",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "weekday_id")})
    private Set<Weekday> weekdays;

}
