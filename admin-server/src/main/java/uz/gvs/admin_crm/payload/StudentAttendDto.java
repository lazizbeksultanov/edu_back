package uz.gvs.admin_crm.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class StudentAttendDto {
    private UUID studentId;
    private boolean active;
}
