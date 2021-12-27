package application.streaming.contracts;

import reactor.core.publisher.Flux;

public interface IFlow<TInput, TOutput> {
    Flux<TOutput> run(Flux<TInput> input);
}
