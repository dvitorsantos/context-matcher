package lsdi.Controllers;

import lsdi.Constants.ObjectTypes;
import lsdi.DataTransferObjects.EventProcessNetworkRequest;
import lsdi.DataTransferObjects.EventProcessNetworkResponse;
import lsdi.DataTransferObjects.NodeRequest;
import lsdi.DataTransferObjects.TaggedObjectResponse;
import lsdi.Entities.EventProcessNetwork;
import lsdi.Entities.Match;
import lsdi.Entities.Node;
import lsdi.Entities.Rule;
import lsdi.Exceptions.MatchNotFoundException;
import lsdi.Exceptions.TaggerException;
import lsdi.Services.CDPOService;
import lsdi.Services.EventProcessNetworkService;
import lsdi.Services.MatchService;
import lsdi.Services.TaggerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    TaggerService taggerService;

    @Autowired
    CDPOService cdpoService;

    @Autowired
    MatchService matchService;

    @Autowired
    EventProcessNetworkService eventProcessNetworkService;

    @PostMapping("/find/nodes_to_epn")
    public ResponseEntity<Object> find(@RequestBody EventProcessNetworkRequest epn) {
        try {
            List<Rule> rules = epn.getRules();
            List<Match> matches = new ArrayList<>();

            rules.forEach(rule -> {
                Node node = findMatchingNodeToRule(rule); //throws MatchNotFoundException
                matches.add(new Match(rule, node, true));
            });

            EventProcessNetwork eventProcessNetwork = epn.toEntity();
            eventProcessNetwork.setMatched(true);
            EventProcessNetworkResponse eventProcessNetworkResponse =
                    EventProcessNetworkResponse.fromEventProcessNetwork(eventProcessNetworkService.save(eventProcessNetwork));
            List<Match> savedMatches = matchService.saveAll(matches);
            eventProcessNetworkResponse.setMatches(savedMatches);

            return ResponseEntity.status(HttpStatus.OK).body(eventProcessNetworkResponse);
        } catch (MatchNotFoundException matchNotFoundException) {
            EventProcessNetwork eventProcessNetwork = epn.toEntity();
            eventProcessNetwork.setMatched(false);
            EventProcessNetworkResponse eventProcessNetworkResponse =
                EventProcessNetworkResponse.fromEventProcessNetwork(eventProcessNetworkService.save(eventProcessNetwork));

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(eventProcessNetworkResponse);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Event process network commit id must be unique.");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sorry! Something went wrong.");
        }
    }

    @PostMapping("/find/epn_to_node")
    public ResponseEntity<Object> find(@RequestBody NodeRequest node) {
        EventProcessNetworkResponse eventProcessNetworkResponse = new EventProcessNetworkResponse();
        try {
            //get all matches of the node
            List<Match> matches = matchService.findAllByNodeUuid(node.getUuid());
            matches.forEach(match -> {
                Rule rule = match.getRule();

                //get all nodes that match the rule tag expression
                List<TaggedObjectResponse> taggedObjects = Arrays.asList(taggerService.getTaggedObjectByTagExpression(rule.getTagFilter()));
                List<Node> nodes = taggedObjects.stream().map(TaggedObjectResponse::toNode).toList();

                if (!nodes.contains(node.toEntity())) {
                    eventProcessNetworkResponse.setMatched(false);
                }
            });

            return ResponseEntity.status(HttpStatus.OK).body(eventProcessNetworkResponse);
        } catch (TaggerException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(eventProcessNetworkResponse);
        }
    }

    public Node findMatchingNodeToRule(Rule rule) throws MatchNotFoundException {
        String tagExpression = rule.getTagFilter();
        TaggedObjectResponse[] taggedObjects = taggerService.getTaggedObjectByTagExpression(tagExpression);

        taggedObjects = Arrays.stream(taggedObjects)
                .filter(taggedObject ->
                        taggedObject.getType().equals(ObjectTypes.ObjectTypes.get(rule.getLevel())))
                .toArray(TaggedObjectResponse[]::new);

        if (taggedObjects.length == 0)
            throw new MatchNotFoundException("No matching objects found for rule: " + rule.getName());

        List<Node> nodes = Arrays.stream(taggedObjects)
                .map(TaggedObjectResponse::toNode)
                .toList();

        return nodes.get(0);
    }

//    public String revalidateNode(@RequestBody Node node) {
//        List<Match> match = matchService.findAllByNodeUuid(node.getUuid());
//
//        if (match.isEmpty())
//            return
//
//        Match matchFound = match.get();
//        Rule ruleFound = matchFound.getRule();
//        try {
//            Rule rule = cdpoService.getRuleByUuid(ruleFound.getUuid());
//
//            return rule.toString();
//        } catch (TaggerException e) {
//            return e.getMessage();
//        }
//    }
}
