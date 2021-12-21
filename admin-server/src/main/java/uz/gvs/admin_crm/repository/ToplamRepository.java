package uz.gvs.admin_crm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.gvs.admin_crm.entity.Region;
import uz.gvs.admin_crm.entity.Toplam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ToplamRepository extends JpaRepository<Toplam, Integer> {
    List<Toplam> findAllByActive(boolean active, Pageable pageable);

    List<Toplam> findAllByActive(boolean active);

    @Query(nativeQuery = true, value = "select cast(t.id as varchar), t.name, cs.name as kurs_nomi, cast(cs.id as varchar) as kurs_id, " +
            "       cast(t2.id as varchar) as teacher_id," +
            "       (select u.full_name from users u where cast(t2.user_id as varchar)=cast(u.id as varchar)) as teacher_name," +
            "       t.time," +
            "       cast(array(select json_agg(w.weekday_name)  from toplam_weekdays" +
            "        inner join toplam t on t.id = toplam_weekdays.toplam_id" +
            "        inner join weekday w on w.id = toplam_weekdays.weekdays_id) as varchar)," +
            "       (select count(*) from client_status_connect csc where csc.toplam is true and cast(csc.status_id as integer)=t.id) as soni " +
            "from toplam t" +
            "    inner join course cs on cs.id = t.course_id" +
            "    inner join teacher t2 on t.teacher_id = t2.id" +
            " where t.active is true")
    List<Object> getToplamForSelect();

    @Query(nativeQuery = true, value = "select t.id, (select count(*) from client_status_connect csc where csc.toplam is true and cast(csc.status_id as integer)=t.id) as soni from toplam t where active is true")
    List<Object> getToplamCount();

    @Query(nativeQuery = true, value = "select cast(cl.id as varchar), cl.full_name, cl.phone_number, cast(csc.updated_at as varchar) as vaqt from client cl " +
            "inner join client_status_connect csc on cl.id = csc.client_id " +
            "where csc.toplam is true and cast(csc.status_id as integer)=:toplamId")
    List<Object> getClientListByToplam(Integer toplamId);

}
