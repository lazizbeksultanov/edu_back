package uz.gvs.admin_crm.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.gvs.admin_crm.entity.Cashback;

import java.util.List;

public interface CashbackRepository extends JpaRepository<Cashback, Integer> {
    boolean existsByPriceEquals(double price);
    boolean existsByPriceEqualsAndIdNot(double price, Integer id);

    @Query(nativeQuery = true, value = "select * from cashback where active is true and price=(select max(price) from cashback where active is true and  price<=:miqdor) " )
    Cashback getByPrice(Double miqdor);
}