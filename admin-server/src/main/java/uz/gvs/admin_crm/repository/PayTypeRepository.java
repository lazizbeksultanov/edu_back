package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gvs.admin_crm.entity.PayType;

public interface PayTypeRepository extends JpaRepository<PayType, Integer> {
    boolean existsByNameEqualsIgnoreCase(String name);
    boolean existsByNameEqualsIgnoreCaseAndIdNot(String name, Integer id);
}
