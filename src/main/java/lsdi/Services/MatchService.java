package lsdi.Services;

import lsdi.Entities.Match;
import lsdi.Repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {
    @Autowired
    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Match save(Match match) {
        return this.matchRepository.save(match);
    }

    public Optional<Match> findByNodeUuid(String nodeUuid) {
        return this.matchRepository.findByNodeUuid(nodeUuid);
    }
}
