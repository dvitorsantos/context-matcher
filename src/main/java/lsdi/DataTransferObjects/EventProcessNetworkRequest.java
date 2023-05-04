package lsdi.DataTransferObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lsdi.Entities.EventProcessNetwork;
import lsdi.Entities.Rule;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventProcessNetworkRequest {
    @JsonProperty("commit_id")
    private String commitId;
    private String version;
    private Boolean enabled;
    private String qos;
    private Boolean atomic;
    private List<RuleRequestResponse> rules;
    @Nullable
    @JsonProperty("webhook_url")
    String webhookUrl;

    public EventProcessNetwork toEntity() {
        List<Rule> epnRules = rules.stream().map(RuleRequestResponse::toEntity).toList();
        EventProcessNetwork epn = new EventProcessNetwork(
                this.commitId,
                this.version,
                this.enabled,
                this.qos,
                this.atomic,
                epnRules,
                this.webhookUrl);
        epnRules.forEach(rule -> rule.setEventProcessNetwork(epn));
        return epn;
    }
}
