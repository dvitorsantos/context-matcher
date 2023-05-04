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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/context")
public class ContextController {
    @Autowired
    MatchService matchService;
    @Autowired
    CDPOConnector cdpoService;

    @PostMapping()
    public ResponseEntity<Object> context(@RequestBody ContextDataRequest contextData) {
        List<Match> matches = matchService.findAllByNodeUuid(contextData.getHostUuid());

        for (Match match : matches) {
            Rule rule = match.getRule();
            if (rule.getRequirements() == null) continue;

            if (rule.getRequirements().getLocationArea() != null) {
                if (contextData.getLocation().isInArea(rule.getRequirements().getLocationArea())) {
                    if (match.getStatus().equals(MatchStatus.UNMATCHED)) {
                        match(match, rule);
                    }
                } else {
                    if (match.getStatus().equals(MatchStatus.MATCHED)) {
                        unmatch(match, rule);
                        continue;
                    }
                }
            }

            if (rule.getRequirements().getStartTime() != null && rule.getRequirements().getEndTime() != null) {
                LocalTime time = contextData.getTimestamp().toLocalTime();
                if (time.isBefore(rule.getRequirements().getEndTime()) &&
                        time.isAfter(rule.getRequirements().getStartTime())) {
                    if (match.getStatus().equals(MatchStatus.UNMATCHED)) {
                        match(match, rule);
                    }
                } else {
                    if (match.getStatus().equals(MatchStatus.MATCHED)) {
                        unmatch(match, rule);
                        continue;
                    }
                }
            }

            if (rule.getRequirements().getStartDate() != null && rule.getRequirements().getEndDate() != null) {
                LocalDate date = contextData.getTimestamp().toLocalDate();
                if (date.isBefore(rule.getRequirements().getEndDate()) &&
                        date.isAfter(rule.getRequirements().getStartDate())) {
                    if (match.getStatus().equals(MatchStatus.UNMATCHED)) {
                        match(match, rule);
                    }
                } else {
                    if (match.getStatus().equals(MatchStatus.MATCHED)) {
                        unmatch(match, rule);
                    }
                }
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("Context data received.");
    }

    private void match(Match match, Rule rule) {
        cdpoService.deployRule(match.getHost(), rule.getUuid());
        match.setStatus(MatchStatus.MATCHED);
        matchService.save(match);
    }

    private void unmatch(Match match, Rule rule) {
        cdpoService.undeployRule(match.getHost(), rule.getUuid());
        match.setStatus(MatchStatus.UNMATCHED);
        matchService.save(match);
    }
}
