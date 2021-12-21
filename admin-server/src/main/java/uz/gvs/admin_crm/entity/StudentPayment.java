package uz.gvs.admin_crm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudentPayment extends AbsEntity {
    @ManyToOne
    private PayType payType;
    @ManyToOne
    private Student student;
    private double sum;
    private Date payDate;
    @Column(columnDefinition = "text")
    private String comment;
    private double cashSum;
    @ManyToOne
    private Group group;
    @ManyToOne
    private Cashback cashback;


}
