package uz.gvs.admin_crm.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.Cashback;
import uz.gvs.admin_crm.entity.Group;
import uz.gvs.admin_crm.entity.PayType;
import uz.gvs.admin_crm.entity.Student;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentPaymentDto {
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID id;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PayType payType;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Student student;
    private double sum;
    private String payDate;
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String comment;
    private double cashSum;
    private Integer groupId;
    private UUID studentId;
    private Integer payTypeId;
    private Group group;
    private Cashback cashback;


    public StudentPaymentDto(UUID id, PayType payType, Student student, double sum, String payDate, String comment) {
        this.id = id;
        this.payType = payType;
        this.student = student;
        this.sum = sum;
        this.payDate = payDate;
        this.comment = comment;
    }

    public StudentPaymentDto(UUID id, PayType payType, Student student, Cashback cashback,Double cashSum,double sum, String payDate, String comment, Group group) {
        this.id = id;
        this.payType = payType;
        this.student = student;
        this.sum = sum;
        this.payDate = payDate;
        this.comment = comment;
        this.group = group;
        this.cashback = cashback;
        this.cashSum = cashSum;
    }
}
