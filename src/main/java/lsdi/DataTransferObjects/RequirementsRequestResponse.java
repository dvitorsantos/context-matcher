package lsdi.DataTransferObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lsdi.Entities.Requirements;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RequirementsRequestResponse {
    @Nullable
    @JsonProperty("location_area")
    private LocationAreaRequestResponse locationArea;
    @Nullable
    @JsonProperty("start_time")
    private LocalTime startTime;
    @Nullable
    @JsonProperty("end_time")
    private LocalTime endTime;
    @Nullable
    @JsonProperty("start_date")
    private LocalDate startDate;
    @Nullable
    @JsonProperty("end_date")
    private LocalDate endDate;

    public static RequirementsRequestResponse fromEntity(Requirements requirements) {
        RequirementsRequestResponse requirementsRequestResponse = new RequirementsRequestResponse();
        requirementsRequestResponse.setLocationArea(LocationAreaRequestResponse.fromEntity(requirements.getLocationArea()));
        requirementsRequestResponse.setStartTime(requirements.getStartTime());
        requirementsRequestResponse.setEndTime(requirements.getEndTime());
        requirementsRequestResponse.setStartDate(requirements.getStartDate());
        requirementsRequestResponse.setEndDate(requirements.getEndDate());
        return requirementsRequestResponse;
    }

    public Requirements toEntity() {
        Requirements requirements = new Requirements();
        requirements.setLocationArea(this.getLocationArea().toEntity());
        requirements.setStartTime(this.getStartTime());
        requirements.setEndTime(this.getEndTime());
        requirements.setStartDate(this.getStartDate());
        requirements.setEndDate(this.getEndDate());
        return requirements;
    }
}
