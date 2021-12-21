package uz.gvs.admin_crm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.template.AbsEntity;
import uz.gvs.admin_crm.entity.template.AbsIntEntity;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Toplam extends AbsIntEntity {

    private String name;
    @ManyToOne
    private Course course;
    @ManyToOne
    private Teacher teacher;
    @ManyToMany
    @JoinTable(name = "toplam_weekdays",
            joinColumns = {@JoinColumn(name = "toplam_id")},
            inverseJoinColumns = {@JoinColumn(name = "weekday_id")})
    private Set<Weekday> weekdays;

    private String time;
    private boolean active;
}
