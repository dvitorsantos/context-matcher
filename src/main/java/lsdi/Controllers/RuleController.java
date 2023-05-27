package lsdi.Controllers;

import lsdi.DataTransferObjects.RuleRequestResponse;
import lsdi.Entities.Rule;
import lsdi.Services.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RuleController {
    @Autowired
    RuleService ruleService;
    @GetMapping("/rule/{hostUuid}/{ruleUuid}")
    public Optional<RuleRequestResponse> findByHostUuidAndRuleUuid(@PathVariable String hostUuid, @PathVariable String ruleUuid) {
        Optional<Rule> rules = ruleService.find(hostUuid, ruleUuid);
        return rules.map(RuleRequestResponse::fromEntity);
    }
}
