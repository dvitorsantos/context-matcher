package lsdi.Controllers;

import lsdi.Connector.CDPOConnector;
import lsdi.DataTransferObjects.ContextDataRequest;
import lsdi.Entities.Match;
import lsdi.Entities.Requirements;
import lsdi.Entities.Rule;
import lsdi.Enums.MatchStatus;
import lsdi.Services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/context")
public class ContextController {
    @Autowired
    MatchService matchService;

    @Autowired
    CDPOConnector cdpoService;

    @PostMapping("/new")
    public ResponseEntity<Object> newContext(@RequestBody ContextDataRequest contextData) {
        List<Match> matches = matchService.findAllByNodeUuid(contextData.getHostUuid());

        for (Match match : matches) {
            Rule rule = match.getRule();
           if (contextData.getLocation().isInArea(rule.getRequirements().getLocationArea())) {
               if (match.getStatus().equals(MatchStatus.UNMATCHED)) {
                   cdpoService.deployRule(match.getHost(), rule.getUuid());
                   match.setStatus(MatchStatus.MATCHED);
                   matchService.save(match);
               }
           } else {
               if (match.getStatus().equals(MatchStatus.MATCHED)) {
                   cdpoService.undeployRule(match.getHost(), rule.getUuid());
                   match.setStatus(MatchStatus.UNMATCHED);
                   matchService.save(match);
               }
           }
        }

        return ResponseEntity.status(HttpStatus.OK).body("Context data received.");
    }
}
