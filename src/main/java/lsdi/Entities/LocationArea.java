package lsdi.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode
public class LocationArea {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
    private Double area;
    private Double latitude;
    private Double longitude;
    @OneToOne(mappedBy = "locationArea", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Requirements requirements;
}
