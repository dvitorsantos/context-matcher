package lsdi.DataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lsdi.Entities.Match;
import lsdi.Enums.MatchStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchRequestResponse {
    public String uuid;
    public String ruleUuid;
    public String hostUuid;
    public MatchStatus status;

    public static MatchRequestResponse fromEntity(Match match) {
        return new MatchRequestResponse(
                match.getUuid(),
                match.getRule().getUuid(),
                match.getHost(),
                match.getStatus()
        );
    }
}
