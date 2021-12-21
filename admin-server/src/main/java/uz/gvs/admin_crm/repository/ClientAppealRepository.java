package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.gvs.admin_crm.entity.ClientAppeal;
import uz.gvs.admin_crm.entity.enums.ClientStatusEnum;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientAppealRepository extends JpaRepository<ClientAppeal, UUID> {
    Optional<ClientAppeal> findByClient_idAndStatusEnum(UUID client_id, ClientStatusEnum statusEnum);

    List<ClientAppeal> findAllByClient_id(UUID client_id);

    @Query(nativeQuery = true, value = "select cast(cl.id as varchar) as client_id, cast(ca.status_enum as varchar) as status_enum,  " +
            "cs.name as status_nomi, cast(cs.id as varchar) as status_id, cast(ca.updated_at as varchar) as vaqt, cast(ca.id as varchar) as tarix_id" +
            " from client cl" +
            " inner join client_appeal ca on cl.id = ca.client_id" +
            " inner join client_status cs on cast(ca.status_id as integer) = cs.id" +
            " where cl.id=:mijoz and ca.status_enum!='COLLECTION'")
    List<Object> getClientAppealList(UUID mijoz);

    @Query(nativeQuery = true, value = "select cast(cl.id as varchar) as client_id, cast(ca.status_enum as varchar) as status_enum," +
            " t.name as toplam_nomi, cast(t.id as varchar) as toplam_id, " +
            "cast(ca.updated_at as varchar) as vaqt, cast(ca.id as varchar) as tarix_id " +
            "from client cl " +
            "inner join client_appeal ca on cl.id = ca.client_id " +
            "inner join toplam t on cast(ca.status_id as integer)=t.id " +
            "where cl.id=:mijoz and ca.status_enum='COLLECTION'")
    List<Object> getClientAppealListByToplam(UUID mijoz);
}
