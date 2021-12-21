package uz.gvs.admin_crm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.enums.Gender;
import uz.gvs.admin_crm.entity.template.AbsEntity;
import uz.gvs.admin_crm.entity.template.AbsNameEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Client extends AbsEntity {
    private String fullName;
    private String phoneNumber;

    private int age;
    @ManyToOne
    private Region region;
    @ManyToOne
    private Reklama reklama;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(columnDefinition = "text")
    private String description;

    public Client(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public Client(String fullName, String phoneNumber, String description, Region region, Gender gender) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.region = region;
        this.gender = gender;
    }

    public Client(String fullName, String phoneNumber, String description) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }
}
