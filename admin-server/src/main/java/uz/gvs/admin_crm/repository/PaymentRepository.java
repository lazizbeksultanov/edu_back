package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gvs.admin_crm.entity.Payment;
import uz.gvs.admin_crm.entity.Room;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
