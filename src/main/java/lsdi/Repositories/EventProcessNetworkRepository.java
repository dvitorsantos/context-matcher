package lsdi.Repositories;

import lsdi.Entities.EventProcessNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventProcessNetworkRepository extends JpaRepository<EventProcessNetwork, String> {

    List<EventProcessNetwork> findAllByMatched(boolean matched);

    @Query("SELECT epn FROM EventProcessNetwork epn " +
            "JOIN Rule rule ON epn.uuid = rule.eventProcessNetwork.uuid " +
            "JOIN Match match ON rule.uuid = match.rule.uuid WHERE match.host = ?1")
    List<EventProcessNetwork> findAllByHost(String nodeUuid);
}
