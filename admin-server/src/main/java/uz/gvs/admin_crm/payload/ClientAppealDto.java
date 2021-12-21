package uz.gvs.admin_crm.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.Client;
import uz.gvs.admin_crm.entity.ClientAppeal;
import uz.gvs.admin_crm.entity.ClientStatusConnect;
import uz.gvs.admin_crm.entity.Region;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientAppealDto {
    private UUID id;
    private String statusName;
    private UUID clientId;
    private Integer statusId;
    private String date;
    private String status;
}
