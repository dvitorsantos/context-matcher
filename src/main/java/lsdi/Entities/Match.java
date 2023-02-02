package lsdi.Entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@RequiredArgsConstructor
@Entity
public class Match {
    public Match(String nodeUuid, String ruleUuid, Boolean status) {
        this.nodeUuid = nodeUuid;
        this.ruleUuid = ruleUuid;
        this.status = status;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uuid;
    @Column(name = "node", columnDefinition = "VARCHAR(255)")
    private String nodeUuid;

    @Column(name = "rule", columnDefinition = "VARCHAR(255)")
    private String ruleUuid;

    @Column(name = "status")
    private Boolean status;
}
