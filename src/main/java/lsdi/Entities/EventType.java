package lsdi.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventType {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    String uuid;
    String name;
    @ManyToOne
    Rule rule;
    @OneToMany(mappedBy = "eventType", cascade = CascadeType.ALL)
    List<EventAttribute> eventAttributes;

    public EventType(String name, Rule rule) {
        this.name = name;
        this.rule = rule;
    }
}