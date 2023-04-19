package lsdi.Models;

import lombok.Data;
import lsdi.Entities.LocationArea;

@Data
public class Location {
    private Double latitude;
    private Double longitude;

    public Boolean isInArea(LocationArea locationArea) {
        double raio_terra = 6371;  // em quilômetros

        // Converter coordenadas de graus para radianos
        double lat1 = Math.toRadians(this.getLatitude());
        double lon1 = Math.toRadians(this.getLongitude());
        double lat2 = Math.toRadians(locationArea.getLatitude());
        double lon2 = Math.toRadians(locationArea.getLongitude());

        // Aplicar a fórmula da Haversine
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        double a = Math.sin(dlat/2)*Math.sin(dlat/2) + Math.cos(lat1)*Math.cos(lat2)*Math.sin(dlon/2)*Math.sin(dlon/2);
        double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distancia = raio_terra * c;

        return distancia <= locationArea.getArea();
    }
}
