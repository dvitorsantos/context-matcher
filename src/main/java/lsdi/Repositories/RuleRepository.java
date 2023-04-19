package lsdi.Repositories;

import lsdi.Entities.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuleRepository extends JpaRepository<Rule, String> {
    Optional<Rule> findByUuid(String uuid);
}
