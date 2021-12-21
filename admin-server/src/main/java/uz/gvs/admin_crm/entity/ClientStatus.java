package uz.gvs.admin_crm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.enums.ClientStatusEnum;
import uz.gvs.admin_crm.entity.template.AbsNameEntity;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class ClientStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Column
    private String name;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Column
    private boolean active;
    @Enumerated(EnumType.STRING)
    private ClientStatusEnum clientStatusEnum;
}
