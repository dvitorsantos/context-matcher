package lsdi.DataTransferObjects;

import lombok.Data;
import lsdi.Entities.LocationArea;


@Data
public class LocationAreaRequestResponse {
    private Double radius;
    private Double latitude;
    private Double longitude;
    public static LocationAreaRequestResponse fromEntity(LocationArea locationArea) {
        LocationAreaRequestResponse locationAreaRequestResponse = new LocationAreaRequestResponse();
        locationAreaRequestResponse.setRadius(locationArea.getRadius());
        locationAreaRequestResponse.setLatitude(locationArea.getLatitude());
        locationAreaRequestResponse.setLongitude(locationArea.getLongitude());
        return locationAreaRequestResponse;
    }

    public LocationArea toEntity() {
        LocationArea locationArea = new LocationArea();
        locationArea.setRadius(this.getRadius());
        locationArea.setLatitude(this.getLatitude());
        locationArea.setLongitude(this.getLongitude());
        return locationArea;
    }
}
