package lsdi.DataTransferObjects;

import lombok.Data;
import lsdi.Models.Location;
import lsdi.Models.Performace;

@Data
public class ContextDataRequest {
    private String hostUuid;
    private Location location;
    private Performace performace;
}
