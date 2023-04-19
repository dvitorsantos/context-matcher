package lsdi.Services;

import lsdi.DataTransferObjects.RuleRequestResponse;
import lsdi.Entities.Rule;
import lsdi.Repositories.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Service
public class RuleService {
    @Autowired
    RuleRepository ruleRepository;

    @GetMapping("/rule/{uuid}")
    public Optional<RuleRequestResponse> find(String uuid) {
        Optional<Rule> rule = ruleRepository.findByUuid(uuid);
        return rule.map(RuleRequestResponse::fromEntity);
    }
}
