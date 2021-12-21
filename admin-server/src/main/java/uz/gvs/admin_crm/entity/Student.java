package uz.gvs.admin_crm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.template.AbsEntity;
import uz.gvs.admin_crm.entity.template.AbsNameEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student extends AbsEntity {
    @ManyToOne
    private User user;
    private double balans;
    private String parentPhone;
    @OneToMany
    private Set<StudentGroup> studentGroup;
}
