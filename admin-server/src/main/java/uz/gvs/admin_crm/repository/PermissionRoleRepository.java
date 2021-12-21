package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gvs.admin_crm.entity.PermissionRole;
import uz.gvs.admin_crm.entity.Role;
import uz.gvs.admin_crm.entity.enums.RoleName;

import java.util.Set;


public interface PermissionRoleRepository extends JpaRepository<PermissionRole, Integer> {

}
