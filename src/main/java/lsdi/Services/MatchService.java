package lsdi.Services;

import lsdi.Connector.TaggerConnector;
import lsdi.Constants.ObjectTypes;
import lsdi.DataTransferObjects.TaggedObjectResponse;
import lsdi.Entities.EventProcessNetwork;
import lsdi.Entities.Match;
import lsdi.Enums.MatchStatus;
import lsdi.Models.Node;
import lsdi.Entities.Rule;
import lsdi.Repositories.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final TaggerConnector taggerConnector;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
        this.taggerConnector = new TaggerConnector();
    }

    public Match save(Match match) {
        return this.matchRepository.save(match);
    }

    public List<Match> findAllByNodeUuid(String hostUuid) {
        return this.matchRepository.findAllByHost(hostUuid);
    }

    public List<Match> findAllByEventProcessNetworkUuid(String eventProcessNetworkUuid) {
        return this.matchRepository.findAllByEventProcessNetworkUuid(eventProcessNetworkUuid);
    }

    public List<Match> saveAll(List<Match> matches) {
        return this.matchRepository.saveAll(matches);
    }

    public void delete(Match match) {
        this.matchRepository.delete(match);
    }

    public boolean isValidMatch(Match match) {
        Rule rule = match.getRule();
        List<Node> nodes = findMatchingNodesToRule(rule);

        return nodes.stream()
                .map(Node::getUuid)
                .anyMatch(uuid -> uuid.equals(match.getHost()));
    }

    public List<Node> findMatchingNodesToRule(Rule rule) {
        String tagExpression = rule.getTagFilter();
        TaggedObjectResponse[] taggedObjects = taggerConnector.getTaggedObjectByTagExpression(tagExpression);

        List<TaggedObjectResponse> filteredTaggedObjects = Arrays.stream(taggedObjects)
                .filter(taggedObject -> taggedObject.getType().equals(ObjectTypes.ObjectTypes.get(rule.getLevel())))
                .toList();

        if (filteredTaggedObjects.isEmpty()) {
            return new ArrayList<>();
        }

        return filteredTaggedObjects.stream()
                .map(TaggedObjectResponse::toNode)
                .collect(Collectors.toList());
    }

    public List<Match> findMatchesToEventProcessNetwork(EventProcessNetwork eventProcessNetwork) {
        List<Rule> rules = eventProcessNetwork.getRules();
        List<Match> matches = new ArrayList<>(rules.size());

        for (Rule rule : rules) {
            List<Node> nodes = findMatchingNodesToRule(rule);
            if (nodes.isEmpty())
                return Collections.emptyList();
            else {
                for (Node node : nodes) {
                    Match match = new Match(rule, node, MatchStatus.MATCHED);
                    matches.add(match);
                }

                //sem isso, dá erro de recursão infinita
                if (rule.getRequirements() != null) {
                    rule.getRequirements().setRule(null);
                    rule.getRequirements().getLocationArea().setRequirements(null);
                }
            }
        }

        return matches;
    }
}
