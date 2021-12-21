package uz.gvs.admin_crm.payload;

import lombok.Data;

@Data
public class ClientStatusDto {
    private Integer id;
    private String name;
    private boolean active;
    private String clientStatusEnum;
}
