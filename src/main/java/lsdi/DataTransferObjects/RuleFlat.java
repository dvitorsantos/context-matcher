package lsdi.DataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lsdi.Entities.Rule;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleFlat {
    private String uuid;
    private String name;
    private String description;
    private String tagFilter;
    private String level;
    private String target;
    private String definition;
    private String qos;

    public static RuleFlat fromRule(Rule rule) {
        return new RuleFlat(
                rule.getUuid(),
                rule.getName(),
                rule.getDescription(),
                rule.getTagFilter(),
                rule.getLevel(),
                rule.getTarget(),
                rule.getDefinition(),
                rule.getQos()
        );
    }
}
