package uz.gvs.admin_crm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import uz.gvs.admin_crm.entity.enums.ClientStatusEnum;
import uz.gvs.admin_crm.entity.template.AbsNameEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class ClientAppeal {
    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @UpdateTimestamp
    private Timestamp updatedAt;
    @ManyToOne
    private Client client;
    private String statusId;
    @Enumerated(EnumType.STRING)
    private ClientStatusEnum statusEnum;

}
