package lsdi.DataTransferObjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lsdi.Entities.EventProcessNetwork;
import lsdi.Entities.Node;
import lsdi.Entities.Rule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public EventProcessNetwork toEntity() {
        return new EventProcessNetwork(
                this.commitId,
                this.version,
                this.enabled,
                this.qos,
                this.atomic,
                rules.stream().map(RuleRequestResponse::toEntity).toList());
    }
}
