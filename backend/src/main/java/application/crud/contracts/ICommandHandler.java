package application.crud.contracts;

import org.reactivestreams.Publisher;

public interface ICommandHandler<TRequest, TResponse> {
    Publisher<TResponse> execute(TRequest query);
}
