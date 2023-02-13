package lsdi.DataTransferObjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lsdi.Entities.EventProcessNetwork;
import lsdi.Entities.Node;
import lsdi.Entities.Rule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class EventProcessNetworkRequest {
    private String commitId;
    private String version;
    private String startTime;
    private String endTime;
    private Boolean enabled;
    private String qos;
    private Boolean atomic;

    private List<Rule> rules;

    public EventProcessNetwork toEntity() {
        return new EventProcessNetwork(
                this.commitId,
                this.version,
                this.startTime,
                this.endTime,
                this.enabled,
                this.qos,
                this.atomic,
                this.rules);
    }
}
