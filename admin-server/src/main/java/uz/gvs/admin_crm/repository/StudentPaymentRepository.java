package uz.gvs.admin_crm.repository;


import antlr.collections.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import uz.gvs.admin_crm.entity.Student;
import uz.gvs.admin_crm.entity.StudentPayment;

import java.util.UUID;

public interface StudentPaymentRepository extends JpaRepository<StudentPayment, UUID>{
    Page<StudentPayment> findAllByStudent_id(UUID student_id, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from student_payment where student_payment.cashback_id != 0 " )
    Page<StudentPayment> getStudentPaymentByCashback(Pageable pageable);

}
