package lsdi.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Requirements {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
    @OneToOne(mappedBy = "requirements", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Rule rule;
    @Nullable
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LocationArea locationArea;
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
}
