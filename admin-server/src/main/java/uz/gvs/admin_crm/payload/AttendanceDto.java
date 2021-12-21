package uz.gvs.admin_crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDto {
    private UUID teacherId;
    private Integer groupId;
    private List<StudentAttendDto> studentList;
    private Date date;
}
