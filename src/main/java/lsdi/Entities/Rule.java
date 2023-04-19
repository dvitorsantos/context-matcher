package lsdi.Entities;

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
    private String webhookUrl;
    @ManyToOne
    private EventProcessNetwork eventProcessNetwork;
    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL)
    private List<EventType> eventType;
    @OneToOne(mappedBy = "rule", cascade = CascadeType.ALL)
    private Match match;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Requirements requirements;
}