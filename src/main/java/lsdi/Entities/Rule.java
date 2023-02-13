package lsdi.Entities;

import jakarta.persistence.*;
import lombok.*;

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
}
