package uz.gvs.admin_crm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.template.AbsNameEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "region_id"})
})
public class Region extends AbsNameEntity {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @ManyToOne
    private Region region;

    public Region(Integer id, String name, String description, boolean active, Region region) {
        super(id, name, description, active);
        this.region = region;
    }

    public Region(String name, String description, boolean active, Region region) {
        super(name, description, active);
        this.region = region;
    }
}
