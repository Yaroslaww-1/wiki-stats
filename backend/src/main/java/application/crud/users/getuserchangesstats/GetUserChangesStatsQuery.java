package application.crud.users.getuserchangesstats;

public record GetUserChangesStatsQuery(
        String userName,
        Long windowDurationInMinutes,
        Long stepInMinutes
) { }