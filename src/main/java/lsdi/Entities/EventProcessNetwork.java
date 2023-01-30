package lsdi.Entities;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventProcessNetwork {
    private String commitId;
    private String version;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean enabled;
    private String qos;
    private Boolean atomic;

    private List<Rule> rules;
}
