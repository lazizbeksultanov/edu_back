package uz.gvs.admin_crm.entity.template;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class AbsEntity2 {
    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2",strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @CreationTimestamp
    private Timestamp createdAt;
    @CreatedBy
    private UUID createdBy;
}
