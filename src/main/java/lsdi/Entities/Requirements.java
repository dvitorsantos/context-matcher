package lsdi.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Requirements {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
    private Double minBattery = 0.0;
    private Double minCpu = 100.0;
    private Double minRam = 100.0;
    @OneToOne(mappedBy = "requirements", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Rule rule;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LocationArea locationArea;
}
