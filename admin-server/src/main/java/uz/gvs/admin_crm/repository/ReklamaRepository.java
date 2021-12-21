package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gvs.admin_crm.entity.Reklama;

import java.util.List;


public interface ReklamaRepository extends JpaRepository<Reklama, Integer> {

    boolean existsByNameEqualsIgnoreCase(String name);

    List<Reklama> findAllByActiveIsTrue();

}
