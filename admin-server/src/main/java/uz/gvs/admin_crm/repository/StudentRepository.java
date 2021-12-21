package uz.gvs.admin_crm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.gvs.admin_crm.entity.Student;
import uz.gvs.admin_crm.entity.Toplam;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    @Query(nativeQuery = true, value = "select * from student where balans < 0 " )
    Page<Student> getDebtorStudents(Pageable pageable);

    List<Student> findAllByStudentGroup_Group_id(Integer studentGroup_group_id);

    boolean deleteByBalans(double balans);


}
