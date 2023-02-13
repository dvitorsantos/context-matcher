package lsdi.Repositories;

import lsdi.Entities.EventProcessNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventProcessNetworkRepository extends JpaRepository<EventProcessNetwork, String> {

}
