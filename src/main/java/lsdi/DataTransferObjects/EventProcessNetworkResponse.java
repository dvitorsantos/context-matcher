package lsdi.DataTransferObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lsdi.Entities.EventProcessNetwork;
import lsdi.Entities.Match;
import lsdi.Entities.Rule;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventProcessNetworkResponse {
    private String uuid;
    @JsonProperty("commit_id")
    private String commitId;
    private String version;
    private Boolean enabled;
    private Boolean matched;
    private String qos;
    private Boolean atomic;
    private List<RuleRequestResponse> rules = new ArrayList<>();
    private List<MatchRequestResponse> matches = new ArrayList<>();
    @JsonProperty("webhook_url")
    private String webhookUrl;

    public EventProcessNetworkResponse(String uuid, String commitId, String version, Boolean enabled, Boolean matched, String qos, Boolean atomic, List<Rule> rules, String webhookUrl) {
        this.uuid = uuid;
        this.commitId = commitId;
        this.version = version;
        this.enabled = enabled;
        this.matched = matched;
        this.qos = qos;
        this.atomic = atomic;
        this.setRules(rules);
        this.webhookUrl = webhookUrl;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches.stream().map(MatchRequestResponse::fromEntity).toList();
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules.stream().map(RuleRequestResponse::fromEntity).toList();
    }

    public static EventProcessNetworkResponse fromEventProcessNetwork(EventProcessNetwork eventProcessNetwork) {
        return new EventProcessNetworkResponse(
                eventProcessNetwork.getUuid(),
                eventProcessNetwork.getCommitId(),
                eventProcessNetwork.getVersion(),
                eventProcessNetwork.getEnabled(),
                eventProcessNetwork.getMatched(),
                eventProcessNetwork.getQos(),
                eventProcessNetwork.getAtomic(),
                eventProcessNetwork.getRules(),
                eventProcessNetwork.getWebhookUrl()
        );
    }
}
