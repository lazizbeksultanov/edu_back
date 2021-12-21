package uz.gvs.admin_crm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import uz.gvs.admin_crm.entity.enums.PermissionName;
import uz.gvs.admin_crm.entity.enums.RoleName;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private PermissionName permissionName;

    @Override
    public String getAuthority() {
        return permissionName.name();
    }

    public Permission(PermissionName permissionName) {
        this.permissionName = permissionName;
    }
}
