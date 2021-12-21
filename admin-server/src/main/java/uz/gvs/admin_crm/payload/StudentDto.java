package uz.gvs.admin_crm.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID userId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String fullName;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String phoneNumber;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String parentPhone;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int age;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Region region;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer regionId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String gender;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Attachment avatar;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String birthDate;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<Role> roles;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<Permission> permissions;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private double balans;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<StudentGroup> groupList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UUID> groupIds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StudentGroup studentGroup;

    public StudentDto(UUID id, UUID userId, String fullName, String phoneNumber,String parentPhone, String description, Region region, Integer regionId, String gender, String birthDate, Set<Role> roles, double balans, Set<StudentGroup> groupList) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.parentPhone = parentPhone;
        this.description = description;
        this.region = region;
        this.regionId = regionId;
        this.gender = gender;
        this.birthDate = birthDate;
        this.roles = roles;
        this.balans = balans;
        this.groupList = groupList;
    }

    public StudentDto(UUID id, String fullName, String phoneNumber, StudentGroup group) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.studentGroup = group;
    }

    public StudentDto(UUID id, UUID userId, double balans, String fullName, String phoneNumber, String parentPhone, Region region, Integer regionId, Set<StudentGroup> groupList) {
        this.id = id;
        this.userId = userId;
        this.balans = balans;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.parentPhone = parentPhone;
        this.region = region;
        this.regionId = regionId;
        this.groupList = groupList;
    }
}
