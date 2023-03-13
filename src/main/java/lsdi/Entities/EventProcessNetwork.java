package lsdi.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.RequiredTypes;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventProcessNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
    @Column(unique = true)
    private String commitId;
    private String version;
    private String startTime;
    private String endTime;
    private Boolean enabled;
    private Boolean matched;
    private String qos;
    private Boolean atomic;
    @OneToMany(mappedBy = "eventProcessNetwork", cascade = CascadeType.ALL)
    private List<Rule> rules;

    public EventProcessNetwork(String commitId, String version, Boolean enabled, String qos, Boolean atomic, List<Rule> rules) {
        this.commitId = commitId;
        this.version = version;
        this.enabled = enabled;
        this.qos = qos;
        this.atomic = atomic;
        this.rules = rules;
    }
}
