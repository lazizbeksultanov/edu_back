package uz.gvs.admin_crm.entity.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class AbsNameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Column
    private String name;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Column(columnDefinition = "text")
    private String description;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Column
    private boolean active;

    public AbsNameEntity(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }
}

