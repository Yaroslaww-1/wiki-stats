package application.users.getusereditsstats;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.LocalDateTime;
import java.util.List;

record UserEditsStatsPart(
        Long index,
        Long edits,
        Long durationInMinutes,
        String endTimestamp
) {}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record UserEditsStatsDto(
        List<UserEditsStatsPart> parts
) {}
