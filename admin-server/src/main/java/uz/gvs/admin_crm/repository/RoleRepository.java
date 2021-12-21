package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.gvs.admin_crm.entity.Role;
import uz.gvs.admin_crm.entity.enums.RoleName;

import java.util.Set;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    Set<Role> findAllByRoleName(RoleName roleName);

}
