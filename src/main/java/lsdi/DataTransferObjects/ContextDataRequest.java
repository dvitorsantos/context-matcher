package lsdi.DataTransferObjects;

import lombok.Data;
import lsdi.Models.Location;
import lsdi.Models.Performace;

import java.time.LocalDateTime;

@Data
public class ContextDataRequest {
    private String hostUuid;
    private Location location;
    private LocalDateTime timestamp;
}
