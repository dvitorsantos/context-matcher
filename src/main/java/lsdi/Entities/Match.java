package lsdi.Entities;

import jakarta.persistence.*;
import lombok.*;
import lsdi.Models.Node;

@Entity
@Data
@RequiredArgsConstructor
public class Match {
    public Match(Rule rule, Node node, Boolean status) {
        this.rule = rule;
        this.host = node == null ? null : node.getUuid();
        this.status = status;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
    @ManyToOne
    @JoinColumn(name = "rule_uuid")
    private Rule rule;
    @Column(name = "host_uuid")
    private String host;
    @Column(name = "status")
    private Boolean status;
}
