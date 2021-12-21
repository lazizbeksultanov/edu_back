package uz.gvs.admin_crm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.gvs.admin_crm.entity.Region;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region,Integer> {
    boolean existsByNameEqualsIgnoreCaseAndRegion(String name, Region region);

    boolean existsByNameEqualsIgnoreCaseAndRegionAndIdNot(String name, Region region, Integer id);

    boolean existsByNameEqualsIgnoreCaseAndRegionId(String name, Integer region_id);

    @Query(nativeQuery = true, value = "select * from region where LOWER(name) like concat('%',:search, '%')")
    List<Region> getSearchRegionList(String search);

    boolean existsByNameEqualsIgnoreCaseAndIdNot(String name, Integer id);

    Page<Region> findAllByActiveIsTrue(Pageable pageable);

//    boolean existsByNameEqualsIgnoreCaseAndIdNotAndRegionIdNot(String name, Integer id, Integer reklama_id);

//    @Query(nativeQuery = true, value = "select EXISTS (select * from reklama where reklama_id is null and lower(name)=:reklamaName  and id<>:reklamaId)")
//    boolean existsCheckName(String reklamaName, Integer reklamaId);

    boolean existsByNameEqualsIgnoreCaseAndIdNotAndRegionId(String name, Integer id, Integer region_id);

//    boolean existsByNameEqualsIgnoreCaseAndReklamaIdNot(String name, Integer reklama_id);

}
