package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gvs.admin_crm.entity.Course;
import uz.gvs.admin_crm.entity.CourseCategory;
import uz.gvs.admin_crm.entity.Payment;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findAllByCourseCategory_id(Integer courseCategory_id);
    List<Course> findAllByActiveIsTrue();
    boolean existsByNameEqualsIgnoreCaseAndCourseCategoryId(String name, Integer courseCategory_id);
}
