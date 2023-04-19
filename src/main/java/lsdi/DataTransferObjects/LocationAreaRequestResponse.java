package lsdi.DataTransferObjects;

import lombok.Data;
import lsdi.Entities.LocationArea;


@Data
public class LocationAreaRequestResponse {
    private Double area;
    private Double latitude;
    private Double longitude;
    public static LocationAreaRequestResponse fromEntity(LocationArea locationArea) {
        LocationAreaRequestResponse locationAreaRequestResponse = new LocationAreaRequestResponse();
        locationAreaRequestResponse.setArea(locationArea.getArea());
        locationAreaRequestResponse.setLatitude(locationArea.getLatitude());
        locationAreaRequestResponse.setLongitude(locationArea.getLongitude());
        return locationAreaRequestResponse;
    }

    public LocationArea toEntity() {
        LocationArea locationArea = new LocationArea();
        locationArea.setArea(this.getArea());
        locationArea.setLatitude(this.getLatitude());
        locationArea.setLongitude(this.getLongitude());
        return locationArea;
    }
}
