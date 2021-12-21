package uz.gvs.admin_crm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.template.AbsIdEntity;
import uz.gvs.admin_crm.entity.template.AbsNameEntity;
import uz.gvs.admin_crm.entity.template.AbsNameEntity2;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class PayType extends AbsNameEntity2 {
}
