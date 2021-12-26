package application.users.getusereditsstats;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

record UserEditsStatsPart(
        Long index,
        Long edits,
        Long durationInDays,
        Integer endYear,
        Integer endDay
) {}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record UserEditsStatsDto(
        List<UserEditsStatsPart> parts
) {}
