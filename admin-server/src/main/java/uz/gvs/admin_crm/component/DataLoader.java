package uz.gvs.admin_crm.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.gvs.admin_crm.entity.Permission;
import uz.gvs.admin_crm.entity.PermissionRole;
import uz.gvs.admin_crm.entity.Role;
import uz.gvs.admin_crm.entity.User;
import uz.gvs.admin_crm.entity.enums.PermissionName;
import uz.gvs.admin_crm.entity.enums.RoleName;
import uz.gvs.admin_crm.repository.PermissionRepository;
import uz.gvs.admin_crm.repository.PermissionRoleRepository;
import uz.gvs.admin_crm.repository.RoleRepository;
import uz.gvs.admin_crm.repository.UserRepository;
import uz.gvs.admin_crm.utils.AppConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    PermissionRoleRepository permissionRoleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Value("${spring.datasource.initialization-mode}")
    private String initMode;

    @Override
    public void run(String... args) throws Exception {
        if (initMode.equals("always")) {
            List<Role> roles = roleRepository.findAll();

            List<PermissionRole> permissionRoles = new ArrayList<>();

            for (PermissionName permissionName : PermissionName.values()) {

                Permission savedPermission = permissionRepository.save(
                        new Permission(permissionName));

                for (RoleName roleName : permissionName.roleNames) {
                    permissionRoles.add(new PermissionRole(
                            getRoleByRoleName(roles, roleName), savedPermission));
                }
            }
            permissionRoleRepository.saveAll(permissionRoles);
            userRepository.save(
                    new User(
                            "Superadmin",
                            "998901234567",
                            passwordEncoder.encode("root123"),
                            new HashSet<Role>(roleRepository.findAllByRoleName(RoleName.SUPER_ADMIN)),
                            new HashSet<Permission>(permissionRepository.findAllByRoleName(RoleName.SUPER_ADMIN.name())),
                            true
                    ));
        }
    }

    private Role getRoleByRoleName(List<Role> roles, RoleName roleName) {
        for (Role role : roles) {
            if (role.getRoleName().equals(roleName))
                return role;
        }
        return null;
    }
}
