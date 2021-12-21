package uz.gvs.admin_crm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.enums.AttandanceEnum;
import uz.gvs.admin_crm.entity.template.AbsEntity;
import uz.gvs.admin_crm.entity.template.AbsNameEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attendance extends AbsEntity {
    @ManyToOne
    private Teacher teacher;
    @ManyToOne
    private Group group;
    @ManyToOne
    private Student student;
    @Enumerated(EnumType.STRING)
    private AttandanceEnum attandanceEnum;
    private Date attendDate;
}
