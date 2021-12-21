package uz.gvs.admin_crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SituationDto {
    private UUID studentId;
    private Integer groupId;
    private Integer newGroupId;
    private String situation;
}
