package uz.gvs.admin_crm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReklamaDto {
    private Integer id;
    private String name;
    private String description;
    private boolean isActive;

    public ReklamaDto(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.isActive = active;
    }
}
