package uz.gvs.admin_crm.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.*;
import uz.gvs.admin_crm.entity.enums.Gender;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDto {
    private UUID id;
    private String fullName;
    private String phoneNumber;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int age;
    private Integer regionId;
    private Region region;
    private String gender;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClientStatusConnect clientStatusConnect;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Client client;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ClientAppealDto> clientAppealList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String time;

    public ClientDto(UUID id, String fullName, String phoneNumber, String time) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.time = time;
    }

    public ClientDto(UUID id, String fullName, String phoneNumber, String description, int age, Integer regionId, Region region, String gender) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.age = age;
        this.regionId = regionId;
        this.region = region;
        this.gender = gender;
    }

    public ClientDto(ClientStatusConnect clientStatusConnect, List<ClientAppealDto> clientAppealList) {
        this.clientStatusConnect = clientStatusConnect;
        this.clientAppealList = clientAppealList;
    }

    public ClientDto(UUID id, String fullName, String phoneNumber, String description, int age, Integer regionId, String gender) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.age = age;
        this.regionId = regionId;
        this.gender = gender;
    }
//    public ClientDto(String fullName, String phoneNumber, String description, int age, Integer regionId, String gender) {
//        this.fullName = fullName;
//        this.phoneNumber = phoneNumber;
//        this.description = description;
//        this.age = age;
//        this.regionId = regionId;
//        this.gender = gender;
//    }
}
//
