package lsdi.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
public class Rule {
    private String uuid;
    private String name;
    private String description;
    private String definition;
    private String qos;
    private String level;
    private String target;
    private String tagFilter;
    private String inputEventTypes;
}
