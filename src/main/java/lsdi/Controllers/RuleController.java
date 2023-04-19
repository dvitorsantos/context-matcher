package lsdi.Controllers;

import lsdi.DataTransferObjects.RuleRequestResponse;
import lsdi.Services.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RuleController {
    @Autowired
    RuleService ruleService;

    @GetMapping("/rule/{uuid}")
    public ResponseEntity<RuleRequestResponse> find(@PathVariable String uuid) {
        Optional<RuleRequestResponse> rule = ruleService.find(uuid);
        return rule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
