package uz.gvs.admin_crm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.gvs.admin_crm.entity.template.AbsEntity2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AttachmentContent extends AbsEntity2 {
    @OneToOne(optional = false)
    private Attachment attachment;
    @Column(nullable = false)
    private byte[] content;
}
