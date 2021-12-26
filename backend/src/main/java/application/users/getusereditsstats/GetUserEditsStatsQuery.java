package application.users.getusereditsstats;

public record GetUserEditsStatsQuery(
        String userName,
        Long windowDurationInMinutes,
        Long stepInMinutes
) { }