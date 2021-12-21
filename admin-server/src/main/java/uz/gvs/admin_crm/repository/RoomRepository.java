package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gvs.admin_crm.entity.PayType;
import uz.gvs.admin_crm.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    boolean existsByNameEqualsIgnoreCase(String name);
    boolean existsByNameEqualsIgnoreCaseAndIdNot(String name, Integer id);
}
