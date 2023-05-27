package lsdi.Controllers;

import lsdi.DataTransferObjects.EventProcessNetworkRequest;
import lsdi.DataTransferObjects.EventProcessNetworkResponse;
import lsdi.DataTransferObjects.NodeRequest;
import lsdi.Entities.EventProcessNetwork;
import lsdi.Entities.Match;
import lsdi.Entities.Rule;
import lsdi.Exceptions.TaggerException;
import lsdi.Connector.CDPOConnector;
import lsdi.Services.EventProcessNetworkService;
import lsdi.Services.MatchService;
import lsdi.Connector.TaggerConnector;

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
    TaggerConnector taggerService;

    @Autowired
    CDPOConnector cdpoService;

    @Autowired
    MatchService matchService;

    @Autowired
    EventProcessNetworkService eventProcessNetworkService;

    @PostMapping("/find/nodes_to_epn")
    public ResponseEntity<Object> find(@RequestBody EventProcessNetworkRequest epn) {
        try {
            EventProcessNetwork eventProcessNetwork = eventProcessNetworkService.save(epn.toEntity());
            EventProcessNetworkResponse eventProcessNetworkResponse =
                    EventProcessNetworkResponse.fromEventProcessNetwork(eventProcessNetwork);
            List<Match> matches = matchService.findMatchesToEventProcessNetwork(eventProcessNetwork);
            if (!matches.isEmpty()) {
                eventProcessNetwork.setMatched(true);
                List<Match> savedMatches = matchService.saveAll(matches);
                eventProcessNetworkResponse.setMatches(savedMatches);
                return ResponseEntity.status(HttpStatus.OK).body(eventProcessNetworkResponse);
            } else {
                eventProcessNetwork.setMatched(false);
                eventProcessNetworkResponse.setMatches(matches);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(eventProcessNetworkResponse);
            }
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Event process network commit id must be unique.");
        } catch (Exception exception) {
            exception.printStackTrace();
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
                if (!matchService.isValidMatch(match)) {
                    Rule rule = match.getRule();
                    EventProcessNetwork eventProcessNetwork = rule.getEventProcessNetwork();
                    eventProcessNetwork.setMatched(false);
                    List<Match> matches = matchService.findAllByEventProcessNetworkUuid(eventProcessNetwork.getUuid());
                    matches.forEach(matchService::delete);
                    eventProcessNetworkService.save(eventProcessNetwork);
                    EventProcessNetworkResponse eventProcessNetworkResponse =
                            EventProcessNetworkResponse.fromEventProcessNetwork(eventProcessNetworkService.save(eventProcessNetwork));
                    eventProcessNetworkResponses.add(eventProcessNetworkResponse);
                }
            });

            List<EventProcessNetwork> eventProcessNetworks = eventProcessNetworkService.findAllByMatched(false);
            for (EventProcessNetwork eventProcessNetwork : eventProcessNetworks) {
                List<Match> matches = matchService.findMatchesToEventProcessNetwork(eventProcessNetwork);

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
        } catch (TaggerException exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sorry! Something went wrong.\n" + exception.getMessage());
        }
    }
}
