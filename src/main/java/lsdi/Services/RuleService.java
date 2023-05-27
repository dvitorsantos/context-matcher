package lsdi.Services;

import lsdi.DataTransferObjects.RuleRequestResponse;
import lsdi.Entities.Rule;
import lsdi.Repositories.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class RuleService {
    @Autowired
    RuleRepository ruleRepository;

    public Optional<Rule> find(String hostUuid, String ruleUuid) {
        return ruleRepository.findByHostUuidAndUuid(hostUuid, ruleUuid);
    }
}
