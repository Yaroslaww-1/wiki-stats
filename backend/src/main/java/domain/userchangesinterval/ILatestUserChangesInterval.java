package domain.userchangesinterval;

import reactor.core.publisher.Mono;

public interface ILatestUserChangesInterval {
    Mono<UserChangesInterval> updateAndReturnLatest(UserChangesInterval userChangesInterval);
}
