package lsdi.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
    private String name;
    private String description;
    private String tagFilter;
    private String level;
    private String target;
    private String definition;
    private String qos;
    @ManyToOne
    private EventProcessNetwork eventProcessNetwork;
    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL)
    private List<EventType> eventType;
}
