package uz.gvs.admin_crm.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.Region;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDto {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer id;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean active;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer regionId;

    public RegionDto(Integer id, String name, String description, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
    }


    private Region region;

//    public RegionDto(String name, String description, boolean active, Region region) {
//        this.name = name;
//        this.description = description;
//        this.active = active;
//        this.region = region;
//    }
}
