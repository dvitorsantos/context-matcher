package lsdi.Repositories;

import lsdi.Entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findAllByHost(String hostUuid);

    @Query("SELECT match FROM Match match " +
            "JOIN Rule rule ON match.rule.uuid = rule.uuid " +
            "JOIN EventProcessNetwork epn ON rule.eventProcessNetwork.uuid = epn.uuid " +
            "WHERE epn.uuid = ?1")
    List<Match> findAllByEventProcessNetworkUuid(String eventProcessNetworkUuid);

}
