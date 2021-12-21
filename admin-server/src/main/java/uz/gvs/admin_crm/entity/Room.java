package uz.gvs.admin_crm.entity;
;
import lombok.Data;
import lombok.EqualsAndHashCode;

import uz.gvs.admin_crm.entity.template.AbsNameEntity2;


import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Room extends AbsNameEntity2 {
}
