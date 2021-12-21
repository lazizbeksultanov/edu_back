package uz.gvs.admin_crm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.gvs.admin_crm.entity.enums.Gender;
import uz.gvs.admin_crm.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User extends AbsEntity implements UserDetails {//test

    private String fullName;
    private String phoneNumber;
    @Column(columnDefinition = "text")
    private String description;
    @ManyToOne
    private Region region;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String password;
    @OneToOne
    private Attachment avatar;

    private Date birthDate;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles;

    @ManyToMany
    @JoinTable(name = "user_permissions",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private Set<Permission> permissions;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean enabled = true;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorityList = new HashSet<>();
        if (permissions != null)
            grantedAuthorityList.addAll(permissions);
        grantedAuthorityList.addAll(roles);
        return grantedAuthorityList;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public User(String fullName, String phoneNumber, String password, Set<Role> roles, Set<Permission> permissions, boolean enabled) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.roles = roles;
        this.permissions = permissions;
        this.enabled = enabled;
    }
}
