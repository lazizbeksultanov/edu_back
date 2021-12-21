package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gvs.admin_crm.entity.Weekday;
import uz.gvs.admin_crm.entity.enums.WeekdayName;

import java.util.Optional;


public interface WeekdayRepository extends JpaRepository<Weekday, Integer> {
    Optional<Weekday> findByWeekdayName(WeekdayName valueOf);
}
