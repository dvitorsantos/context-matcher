package lsdi.Entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class Rule {
    private String name;
    private String description;
    private String definition;
    private String qos;
    private String level;
    private String target;
    private String tagFilter;
    private String[] inputEventTypes;
}
