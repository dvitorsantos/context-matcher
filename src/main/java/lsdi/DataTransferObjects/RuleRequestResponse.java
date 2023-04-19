package lsdi.DataTransferObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lsdi.Entities.*;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleRequestResponse {
    @Nullable
    private String uuid;
    private String name;
    private String description;
    @JsonProperty("tag_filter")
    private String tagFilter;
    private String level;
    private String target;
    private String definition;
    private String qos;
    @JsonProperty("event_type")
    String eventType;
    @JsonProperty("event_attributes")
    Map<String, String> eventAttributes;
    RequirementsRequestResponse requirements;

    public RuleRequestResponse(String uuid, String name, String description, String tagFilter, String level, String target, String definition, String qos) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.tagFilter = tagFilter;
        this.level = level;
        this.target = target;
        this.definition = definition;
        this.qos = qos;
    }

    public Rule toEntity(){
        Rule rule = new Rule();
        rule.setUuid(this.uuid);
        rule.setName(this.name);
        rule.setDescription(this.description);
        rule.setTagFilter(this.tagFilter);
        rule.setLevel(this.level);
        rule.setTarget(this.target);
        rule.setDefinition(this.definition);
        rule.setQos(this.qos);



        //event type and event attributes
        EventType eventType = new EventType();
        eventType.setName(this.getEventType());
        eventType.setRule(rule);
        List<EventAttribute> eventAttributes = new ArrayList<>();
        this.eventAttributes.forEach((key, value) -> {
            EventAttribute eventAttribute = new EventAttribute();
            eventAttribute.setName(key);
            eventAttribute.setType(value);
            eventAttribute.setEventType(eventType);
            eventAttributes.add(eventAttribute);
        });
        eventType.setEventAttributes(eventAttributes);
        rule.setEventType(List.of(eventType));

        //requirements
        Requirements requirements = this.requirements.toEntity();
        LocationArea locationArea = this.requirements.getLocationArea().toEntity();
        locationArea.setRequirements(requirements);
        requirements.setLocationArea(locationArea);

        requirements.setRule(rule);
        rule.setRequirements(requirements);

        return rule;
    }

    public static RuleRequestResponse fromEntity(Rule rule) {
        Map<String, String> eventAttributes = new HashMap<>();
        for (EventType eventType : rule.getEventType()) {
            for (EventAttribute eventAttribute : eventType.getEventAttributes()) {
                eventAttributes.put(eventAttribute.getName(), eventAttribute.getType());
            }
        }
        return new RuleRequestResponse(
                rule.getUuid(),
                rule.getName(),
                rule.getDescription(),
                rule.getTagFilter(),
                rule.getLevel(),
                rule.getTarget(),
                rule.getDefinition(),
                rule.getQos(),
                rule.getEventType().get(0).getName(),
                eventAttributes,
                RequirementsRequestResponse.fromEntity(rule.getRequirements())
        );
    }
}
