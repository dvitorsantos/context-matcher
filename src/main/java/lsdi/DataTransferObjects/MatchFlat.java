package lsdi.DataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lsdi.Entities.Match;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchFlat {
    public String uuid;
    public String ruleUuid;
    public String nodeUuid;
    public Boolean status;

    public static MatchFlat fromMatch(Match match) {
        return new MatchFlat(
                match.getUuid(),
                match.getRule().getUuid(),
                match.getNode(),
                match.getStatus()
        );
    }
}
