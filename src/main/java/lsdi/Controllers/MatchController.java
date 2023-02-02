package lsdi.Controllers;

import lsdi.Constants.ObjectTypes;
import lsdi.DataTransferObjects.EventProcessNetworkRequest;
import lsdi.DataTransferObjects.TaggedObjectResponse;
import lsdi.Entities.Match;
import lsdi.Entities.Node;
import lsdi.Entities.Rule;
import lsdi.Exceptions.MatchNotFoundException;
import lsdi.Exceptions.TaggerException;
import lsdi.Services.MatchService;
import lsdi.Services.TaggerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class MatchController {
    @Autowired
    TaggerService taggerService;

    @Autowired
    MatchService matchService;

    @PostMapping("/findMatch")
    public String findMatch(@RequestBody EventProcessNetworkRequest epn) {
        try {
            Map<Rule, List<Node>> matchingNodesToRules = this.findAllMatchingNodesToRules(epn.getRules());
            Map<Rule, Node> bestMatchesToRules = this.chooseBestMatchesToRules(matchingNodesToRules);
            this.saveMatches(bestMatchesToRules);

            return bestMatchesToRules.toString();
        } catch (MatchNotFoundException matchNotFoundException) {
            return matchNotFoundException.getMessage();
        }
    }

    public Map<Rule, List<Node>> findAllMatchingNodesToRules(List<Rule> rules) throws MatchNotFoundException {
        return new ArrayList<>(rules).stream()
                .collect(Collectors.toMap(rule -> rule, rule -> {

                    //get all objects that match the rule
                    String tagExpression = rule.getTagFilter();
                    TaggedObjectResponse[] taggedObjects = taggerService.getTaggedObjectByTagExpression(tagExpression);

                    //filter out objects that are not of the correct type
                    taggedObjects = Arrays.stream(taggedObjects)
                            .filter(taggedObject ->
                                    taggedObject.getType().equals(ObjectTypes.ObjectTypes.get(rule.getLevel())))
                            .toArray(TaggedObjectResponse[]::new);

                    //throw MatchNotFoundException if no objects are found
                    if (taggedObjects.length == 0)
                        throw new MatchNotFoundException("No matching objects found for rule: " + rule.getName());

                    return Arrays.stream(taggedObjects)
                            .map(TaggedObjectResponse::toNode)
                            .collect(Collectors.toList());
                }));
    }

    private Map<Rule, Node> chooseBestMatchesToRules(Map<Rule, List<Node>> matchingNodesToRules) {
        Map<Rule, Node> matches = new HashMap<>();

        matchingNodesToRules.forEach((rule, nodes) -> {
            nodes.forEach(node -> {
                if (!matches.containsValue(node) && matchService.findByNodeUuid(node.getUuid()).isEmpty())
                    matches.put(rule, node);
            });

            if (!matches.containsKey(rule))
                throw new MatchNotFoundException("No matching objects found for rule: " + rule.getName());
        });

        return matches;
    }

    private void saveMatches(Map<Rule, Node> matches) {
        matches.forEach((rule, node) -> {
            matchService.save(new Match(node.getUuid(), rule.getUuid(), true));
        });
    }
}
