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

import javax.swing.text.html.Option;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
            EventProcessNetworkResponse eventProcessNetworkResponse = new EventProcessNetworkResponse();
            EventProcessNetwork eventProcessNetwork = epn.toEntity();
            List<Match> matches = findMatchesToEventProcessNetwork(eventProcessNetwork);

            if (!matches.isEmpty()) {
                eventProcessNetwork.setMatched(true);
                eventProcessNetworkResponse =
                        EventProcessNetworkResponse.fromEventProcessNetwork(eventProcessNetworkService.save(eventProcessNetwork));
                List<Match> savedMatches = matchService.saveAll(matches);
                eventProcessNetworkResponse.setMatches(savedMatches);
                return ResponseEntity.status(HttpStatus.OK).body(eventProcessNetworkResponse);
            } else {
                eventProcessNetwork.setMatched(false);
                eventProcessNetworkResponse =
                        EventProcessNetworkResponse.fromEventProcessNetwork(eventProcessNetworkService.save(eventProcessNetwork));
                eventProcessNetworkResponse.setMatches(matches);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(eventProcessNetworkResponse);
            }
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Event process network commit id must be unique.");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sorry! Something went wrong.");
        }
    }

    @PostMapping("/find/epns_to_node")
    public ResponseEntity<Object> find(@RequestBody NodeRequest nodeRequest) {
        try {
            taggerService.putTagsInObject(nodeRequest);
            List<EventProcessNetworkResponse> eventProcessNetworkResponses = new ArrayList<>();

            List<Match> matchesOfNode = matchService.findAllByNodeUuid(nodeRequest.getUuid());

            matchesOfNode.forEach(match -> {
                Rule rule = match.getRule();
                List<Node> nodes = findMatchingNodesToRule(rule);

                boolean unMatched = true;
                for (Node node : nodes) {
                    if (node.getUuid().equals(nodeRequest.getUuid())) {
                        unMatched = false;
                        break;
                    }
                }

                if (unMatched) {
                    EventProcessNetwork eventProcessNetwork = rule.getEventProcessNetwork();
                    eventProcessNetwork.setMatched(false);
                    List<Match> matches = matchService.findAllByEventProcessNetworkUuid(eventProcessNetwork.getUuid());
                    matches.forEach(matchService::delete);
                    eventProcessNetworkService.save(eventProcessNetwork);
                    EventProcessNetworkResponse eventProcessNetworkResponse =
                            EventProcessNetworkResponse.fromEventProcessNetwork(eventProcessNetworkService.save(eventProcessNetwork));
                    eventProcessNetworkResponse.setMatches(new ArrayList<>());
                    eventProcessNetworkResponses.add(eventProcessNetworkResponse);
                }
            });

            //FIND ALL EPNS THAT ARE NOT MATCHED, AND FIND MATCHES FOR EACH ONE
            List<EventProcessNetwork> eventProcessNetworks = eventProcessNetworkService.findAllByMatched(false);

            for (EventProcessNetwork eventProcessNetwork : eventProcessNetworks) {
                List<Match> matches = findMatchesToEventProcessNetwork(eventProcessNetwork);

                if (!matches.isEmpty()) {
                    eventProcessNetwork.setMatched(true);
                    EventProcessNetworkResponse eventProcessNetworkResponse =
                            EventProcessNetworkResponse.fromEventProcessNetwork(eventProcessNetworkService.save(eventProcessNetwork));
                    List<Match> savedMatches = matchService.saveAll(matches);
                    eventProcessNetworkResponse.setMatches(savedMatches);
                    eventProcessNetworkResponses.add(eventProcessNetworkResponse);
                }
            }

            if (eventProcessNetworkResponses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No event process networks found.");
            }

            return ResponseEntity.status(HttpStatus.OK).body(eventProcessNetworkResponses);
        } catch (TaggerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sorry! Something went wrong.\n" + e.getMessage());
        }
    }

    public List<Node> findMatchingNodesToRule(Rule rule) throws MatchNotFoundException {
        String tagExpression = rule.getTagFilter();
        TaggedObjectResponse[] taggedObjects = taggerService.getTaggedObjectByTagExpression(tagExpression);

        taggedObjects = Arrays.stream(taggedObjects)
                .filter(taggedObject ->
                        taggedObject.getType().equals(ObjectTypes.ObjectTypes.get(rule.getLevel())))
                .toArray(TaggedObjectResponse[]::new);

        if (taggedObjects.length == 0)
            return new ArrayList<>();

        return Arrays.stream(taggedObjects)
                .map(TaggedObjectResponse::toNode)
                .toList();
    }

    public List<Match> findMatchesToEventProcessNetwork(EventProcessNetwork eventProcessNetwork) {
        List<Match> matches = new ArrayList<>();
        List<Rule> rules = eventProcessNetwork.getRules();
        AtomicBoolean isMatched = new AtomicBoolean(true);

        rules.forEach(rule -> {
            List<Node> nodes = findMatchingNodesToRule(rule);
            if (nodes.isEmpty()) isMatched.set(false);
            else {matches.add(new Match(rule, nodes.get(0), true));}
        });

        return isMatched.get() ? matches : new ArrayList<>();
    }
}
