package lsdi.Entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@RequiredArgsConstructor
@Entity
public class Match {
    public Match(Rule rule, Node node, Boolean status) {
        this.rule = rule;
        this.node = node == null ? null : node.getUuid();
        this.status = status;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
    @ManyToOne
    @JoinColumn(name = "rule_uuid")
    private Rule rule;
    @Column(name = "node_uuid")
    private String node;
    @Column(name = "status")
    private Boolean status;
}
