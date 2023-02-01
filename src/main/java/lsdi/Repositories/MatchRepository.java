package lsdi.Repositories;

import lsdi.Entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<Match> findByNodeUuid(Long nodeUuid);
}
