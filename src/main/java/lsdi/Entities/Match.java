package lsdi.Entities;

import jakarta.persistence.*;
import lombok.*;
import lsdi.Enums.MatchStatus;
import lsdi.Models.Node;

import javax.print.attribute.standard.JobState;

@Entity
@Data
@RequiredArgsConstructor
public class Match {
    public Match(Rule rule, Node node, MatchStatus status) {
        this.rule = rule;
        this.host = node == null ? null : node.getUuid();
        this.status = status;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
    @OneToOne(fetch = FetchType.LAZY)
    private Rule rule;
    @Column(name = "host_uuid")
    private String host;
    @Column(name = "status")
    private MatchStatus status;
}
