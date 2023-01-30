package lsdi.DataTransferObjects;

import lombok.Data;
import lsdi.Entities.Rule;

import java.time.LocalDateTime;
import java.util.List;

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

}
