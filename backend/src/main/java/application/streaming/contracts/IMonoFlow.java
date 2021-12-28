package application.streaming.contracts;

import reactor.core.publisher.Mono;

public interface IMonoFlow<TInput, TOutput> {
    Mono<TOutput> run(TInput input);
}