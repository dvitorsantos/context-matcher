package lsdi.DataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lsdi.Entities.EventProcessNetwork;
import lsdi.Entities.Match;
import lsdi.Entities.Rule;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class EventProcessNetworkResponse {
    private String uuid;
    private String commitId;
    private String version;
    private String startTime;
    private String endTime;
    private Boolean enabled;
    private Boolean matched;
    private String qos;
    private Boolean atomic;
    private List<RuleFlat> rules = new ArrayList<>();
    private List<MatchFlat> matches = new ArrayList<>();

    public EventProcessNetworkResponse(String uuid, String commitId, String version, String startTime, String endTime, Boolean enabled, Boolean matched, String qos, Boolean atomic, List<Rule> rules) {
        this.uuid = uuid;
        this.commitId = commitId;
        this.version = version;
        this.startTime = startTime;
        this.endTime = endTime;
        this.enabled = enabled;
        this.matched = matched;
        this.qos = qos;
        this.atomic = atomic;
        setRules(rules);
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches.stream().map(MatchFlat::fromMatch).toList();
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules.stream().map(RuleFlat::fromRule).toList();
    }

    public static EventProcessNetworkResponse fromEventProcessNetwork(EventProcessNetwork eventProcessNetwork) {
        return new EventProcessNetworkResponse(
                eventProcessNetwork.getUuid(),
                eventProcessNetwork.getCommitId(),
                eventProcessNetwork.getVersion(),
                eventProcessNetwork.getStartTime(),
                eventProcessNetwork.getEndTime(),
                eventProcessNetwork.getEnabled(),
                eventProcessNetwork.getMatched(),
                eventProcessNetwork.getQos(),
                eventProcessNetwork.getAtomic(),
                eventProcessNetwork.getRules()
        );
    }
}
