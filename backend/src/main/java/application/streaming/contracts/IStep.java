package application.streaming.contracts;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public interface IStep<TInput, TOutput> {
    Publisher<TOutput> execute(TInput input);
}
