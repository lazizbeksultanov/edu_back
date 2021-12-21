package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.gvs.admin_crm.entity.Permission;

import java.util.List;


public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    @Query(value = "select * from permission where id in(select permission_id from permission_role where role_id=(select id from role where role_name=:roleName))", nativeQuery = true)
    List<Permission> findAllByRoleName(@Param("roleName") String roleName);
}
