package lsdi.Repositories;

import lsdi.Entities.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuleRepository extends JpaRepository<Rule, String> {
    @Query("SELECT match.rule FROM Match match WHERE match.host = ?1 AND match.rule.uuid = ?2")
    Optional<Rule> findByHostUuidAndUuid(String hostUuid, String uuid);
}
