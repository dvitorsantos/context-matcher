package lsdi.Services;

import lsdi.Entities.Match;
import lsdi.Repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<Match> findAllByNodeUuid(String nodeUuid) {
        return this.matchRepository.findAllByNode(nodeUuid);
    }

    public List<Match> saveAll(List<Match> matches) {
        return this.matchRepository.saveAll(matches);
    }
}
