package lsdi.DataTransferObjects;

import lombok.Data;
import lsdi.Entities.Requirements;

@Data
public class RequirementsRequestResponse {
    private Double minBattery = 0.0;
    private Double minCpu = 100.0;
    private Double minRam = 100.0;
    private LocationAreaRequestResponse locationArea;

    public static RequirementsRequestResponse fromEntity(Requirements requirements) {
        RequirementsRequestResponse requirementsRequestResponse = new RequirementsRequestResponse();
        requirementsRequestResponse.setMinBattery(requirements.getMinBattery());
        requirementsRequestResponse.setMinCpu(requirements.getMinCpu());
        requirementsRequestResponse.setMinRam(requirements.getMinRam());
        requirementsRequestResponse.setLocationArea(new LocationAreaRequestResponse().fromEntity(requirements.getLocationArea()));
        return requirementsRequestResponse;
    }

    public Requirements toEntity() {
        Requirements requirements = new Requirements();
        requirements.setMinBattery(this.getMinBattery());
        requirements.setMinCpu(this.getMinCpu());
        requirements.setMinRam(this.getMinRam());
        requirements.setLocationArea(this.getLocationArea().toEntity());
        return requirements;
    }
}
