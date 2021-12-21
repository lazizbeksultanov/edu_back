package uz.gvs.admin_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gvs.admin_crm.entity.Client;
import uz.gvs.admin_crm.entity.ClientStatusConnect;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface ClientStatusConnectRepository extends JpaRepository<ClientStatusConnect, UUID> {
    Optional<ClientStatusConnect> findByClient_id(UUID client_id);

}
