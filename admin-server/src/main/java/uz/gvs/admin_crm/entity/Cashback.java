package uz.gvs.admin_crm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.aop.aspectj.annotation.LazySingletonAspectInstanceFactoryDecorator;
import org.w3c.dom.stylesheets.LinkStyle;
import uz.gvs.admin_crm.entity.template.AbsNameEntity2;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cashback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private double price;

    @Column
    private double percent;

    @Column
    private boolean active;

    public Cashback(double price, double percent, boolean active) {
        this.price = price;
        this.percent = percent;
        this.active = active;
    }
}

