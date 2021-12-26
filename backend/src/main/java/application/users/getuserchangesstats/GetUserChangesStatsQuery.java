package application.users.getuserchangesstats;

public record GetUserChangesStatsQuery(
        String userName,
        Long windowDurationInMinutes,
        Long stepInMinutes
) { }