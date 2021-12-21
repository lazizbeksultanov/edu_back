package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.gvs.admin_crm.entity.Group;
import uz.gvs.admin_crm.entity.Room;

import java.util.List;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    boolean existsByTeacher_Id(UUID teacher_id);

    List<Group> findAllByTeacher_id(UUID teacher_id);

    boolean existsByNameEqualsIgnoreCaseAndCourseId(String name, Integer id);

    boolean existsByNameEqualsIgnoreCaseAndCourseIdAndIdNot(String name, Integer course_id, Integer id);

    @Query(nativeQuery = true, value = "select * from groups where id=ANY(select group_id from student_group where id=ANY(select student_group_id from student_student_group where student_id=:ketmon))")
    List<Group> getStudentGroupList(UUID ketmon);



}
