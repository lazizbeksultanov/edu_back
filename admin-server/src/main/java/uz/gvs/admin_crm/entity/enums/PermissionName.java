package uz.gvs.admin_crm.entity.enums;

import java.util.Arrays;
import java.util.List;

public enum PermissionName {

    SAVE_REGION("Regionlarni qo'shish va o'zgartirish", Arrays.asList(RoleName.ADMIN, RoleName.SUPER_ADMIN), "Regionlarni boshqarish");

    public List<RoleName> roleNames;
    public String name;
    public String generalName;

    PermissionName(java.util.List<RoleName> roleNameList, String name) {
        this.roleNames = roleNameList;
        this.name = name;
    }

    PermissionName(String name, List<RoleName> roleNameList, String generalName) {
        this.name = name;
        this.roleNames = roleNameList;
        this.generalName = generalName;

    }
}
