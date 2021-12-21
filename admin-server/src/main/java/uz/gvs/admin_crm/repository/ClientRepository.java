package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.gvs.admin_crm.entity.Client;
import uz.gvs.admin_crm.entity.Course;
import uz.gvs.admin_crm.payload.AppealDto;

import java.util.List;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    boolean existsByFullNameEqualsIgnoreCaseAndIdNot(String fullName, UUID id);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsById(UUID client_id);

    @Query(nativeQuery = true, value = "select cast(cl.id as varchar) as client_id, cl.full_name as fish, cl.phone_number as tel, " +
            "       t.id as status_id, t.name as status_name " +
            "from client cl " +
            "         inner join client_status_connect csc on cl.id = csc.client_id " +
            "         inner join toplam t on cast(csc.status_id as integer) = t.id " +
            "where csc.toplam is true " +
            "order by cl.updated_at desc " +
            "limit :size offset (:size * :page)")
    List<Object> getClientByFilterEnumSet(int page, int size);

    @Query(nativeQuery = true, value = "select cast(cl.id as varchar) as client_id, cl.full_name as fish, cl.phone_number as tel, " +
            "       t.id as status_id, t.name as status_name " +
            "from client cl " +
            "         inner join client_status_connect csc on cl.id = csc.client_id " +
            "         inner join toplam t on cast(csc.status_id as integer) = t.id " +
            "where t.id=:toplam_id and csc.toplam is true " +
            "order by cl.updated_at desc " +
            "limit :size offset (:size * :page)")
    List<Object> getClientByFilterToplamStatus(Integer toplam_id, int page, int size);


//    @Query(nativeQuery = true, value = "select cast(cl.id as varchar) as tr, cl.full_name as fish, cl.phone_number as tel, cs.name from client cl where cl.id=ANY(select csc.client_id from client_status_connect csc where (cast(csc.status_id as integer))=ANY(select cs.id from client_status cs where (cast(cs.client_status_enum as varchar ))=(cast(:enumtype as varchar )))) order by cl.updated_at desc limit :size offset (:size*:page)")

    @Query(nativeQuery = true, value = "select cast(cl.id as varchar) as client_id, cl.full_name as fish, cl.phone_number as tel, " +
            "       cs.id as status_id, cs.name as status_name, cs.client_status_enum as enum_type " +
            "from client cl " +
            "         inner join client_status_connect csc on cl.id = csc.client_id " +
            "         inner join client_status cs on cast(csc.status_id as integer) = cs.id " +
            "where csc.toplam is not true and (cast(cs.client_status_enum as varchar)) = (cast(:enumtype as varchar)) " +
            "order by cl.updated_at desc " +
            "limit :size offset (:size * :page)")
    List<Object> getClientByFilterEnumType(String enumtype, int page, int size);

    @Query(nativeQuery = true, value = "select cast(cl.id as varchar) as client_id, cl.full_name as fish, cl.phone_number as tel, " +
            "       cs.id as status_id, cs.name as status_name, cs.client_status_enum as enum_type " +
            "from client cl " +
            "         inner join client_status_connect csc on cl.id = csc.client_id " +
            "         inner join client_status cs on cast(csc.status_id as integer) = cs.id " +
            " where csc.toplam is not true and cs.id=:status_tr and (cast(cs.client_status_enum as varchar)) = (cast(:enumtype as varchar)) " +
            "order by cl.updated_at desc " +
            "limit :size offset (:size * :page)")
    List<Object> getClientByFilterStatus(String enumtype, Integer status_tr, int page, int size);

    @Query(nativeQuery = true, value = "select count(*) as soni " +
            "from client cl " +
            "         inner join client_status_connect csc on cl.id = csc.client_id " +
            "         inner join client_status cs on cast(csc.status_id as integer) = cs.id " +
            "where (cast(cs.client_status_enum as varchar)) = (cast(:enumtype as varchar)) ")
    Integer getCountByEnumType(String enumtype);

    @Query(nativeQuery = true, value = "select count(*) as soni " +
            "from client cl " +
            "         inner join client_status_connect csc on cl.id = csc.client_id " +
            "         inner join client_status cs on cast(csc.status_id as integer) = cs.id " +
            "where cs.id=:status_tr and(cast(cs.client_status_enum as varchar)) = (cast(:enumtype as varchar)) ")
    Integer getCountByStatusType(String enumtype, Integer status_tr);

    @Query(nativeQuery = true, value = "select count(*) as soni " +
            "from client cl " +
            "         inner join client_status_connect csc on cl.id = csc.client_id " +
            "         inner join toplam t on cast(csc.status_id as integer) = t.id " +
            "where csc.toplam is true")
    Integer getCountByEnumSet();

    @Query(nativeQuery = true, value = "select count(*) as soni " +
            "from client cl " +
            "         inner join client_status_connect csc on cl.id = csc.client_id " +
            "         inner join toplam t on cast(csc.status_id as integer) = t.id " +
            "where csc.toplam is true and t.id=:toplam_id")
    Integer getCountByStatusToplam(Integer toplam_id);


    // GET ONE APPEAL FOR SELECT APPEAL PAGE
    @Query(nativeQuery = true, value = " select cast(cl.id as varchar) as client_id, cl.full_name as fish, cl.phone_number as tel, " +
            "       cs.id as status_id, cs.name as status_name, cs.client_status_enum as enum_type, cast(cl.created_at as datetime) as kelgan_vaqti " +
            "from client cl " +
            "         inner join client_status_connect csc on cl.id = csc.client_id " +
            "         inner join client_status cs on cast(csc.status_id as integer) = cs.id " +
            " where cl.id =:clientId ")
    List<Object> getOneClientByFilterEnumType(UUID clientId);

    @Query(nativeQuery = true, value = "select * from client_appeal where client_id=:client_id")
    List<Object> getClientAppealHistoryList(UUID client_id);


}
