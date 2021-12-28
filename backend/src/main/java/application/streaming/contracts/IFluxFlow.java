package application.streaming.contracts;

import reactor.core.publisher.Flux;

public interface IFluxFlow<TInput, TOutput> {
    Flux<TOutput> run(Flux<TInput> input);
}


