package uz.gvs.admin_crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.PayType;
import uz.gvs.admin_crm.entity.Teacher;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherSalaryDto {
    private UUID id;
    private UUID teacherId;
    private Integer payTypeId;
    private Double amount;
    private String amountDate;
    private String description;
    private PayType payType;
    private String teacherName;

    public TeacherSalaryDto(UUID id,UUID teacherId,  Double amount, String amountDate, String description, PayType payType) {
        this.id = id;
        this.teacherId = teacherId;
        this.amount = amount;
        this.amountDate = amountDate;
        this.description = description;
        this.payType = payType;
    }
    public TeacherSalaryDto(UUID id, String teacherName, UUID teacherId,  Double amount, String amountDate, String description, PayType payType) {
        this.id = id;
        this.teacherName = teacherName;
        this.teacherId = teacherId;
        this.amount = amount;
        this.amountDate = amountDate;
        this.description = description;
        this.payType = payType;
    }
}
