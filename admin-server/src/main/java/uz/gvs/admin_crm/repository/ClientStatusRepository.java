package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gvs.admin_crm.entity.ClientStatus;
import uz.gvs.admin_crm.entity.Group;
import uz.gvs.admin_crm.entity.enums.ClientStatusEnum;

import java.util.List;

public interface ClientStatusRepository extends JpaRepository<ClientStatus, Integer> {
    boolean existsByNameEqualsIgnoreCaseAndClientStatusEnum(String name, ClientStatusEnum clientStatusEnum);
    boolean existsByNameEqualsIgnoreCaseAndClientStatusEnumAndIdNot(String name, ClientStatusEnum clientStatusEnum, Integer id);

    List<ClientStatus> findAllByClientStatusEnum(ClientStatusEnum clientStatusEnum);
}
