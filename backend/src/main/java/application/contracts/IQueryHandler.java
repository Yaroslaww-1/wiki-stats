package application.contracts;

import org.reactivestreams.Publisher;

public interface IQueryHandler<TRequest, TResponse> {
    Publisher<TResponse> execute(TRequest query);
}
