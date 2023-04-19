package lsdi.Controllers;

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

    @PostMapping("/new")
    public ResponseEntity<Object> newContext(@RequestBody ContextDataRequest contextData) {
        List<Match> matches = matchService.findAllByNodeUuid(contextData.getHostUuid());

        for (Match match : matches) {
           Requirements requirements = match.getRule().getRequirements();
           if (contextData.getLocation().isInArea(requirements.getLocationArea())) {
               if (match.getStatus().equals(MatchStatus.UNMATCHED)) {
                   //send request to deploy rule
                   match.setStatus(MatchStatus.MATCHED);
               }
           } else {
               if (match.getStatus().equals(MatchStatus.MATCHED)) {
                   //send request to undeploy rule
                   match.setStatus(MatchStatus.UNMATCHED);
               }
           }
        }

        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
