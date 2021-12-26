package application.users.getuserchangesstats;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

record UserChangesStatsPart(
        Long index,
        Long changes,
        Long durationInMinutes,
        String endTimestamp
) {}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record UserChangesStatsDto(
        List<UserChangesStatsPart> parts
) {}
