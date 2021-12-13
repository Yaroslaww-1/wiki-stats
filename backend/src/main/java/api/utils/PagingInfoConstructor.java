package api.utils;

import reactor.netty.http.server.HttpServerRequest;

public class PagingInfoConstructor {
    public final Integer page;
    public final Integer count;

    public PagingInfoConstructor(HttpServerRequest request) {
        String page = request.param("page");
        this.page = page == null ? 0 : Integer.parseInt(page);

        String count = request.param("count");
        this.count = count == null ? 10 : Integer.parseInt(count);
    }
}
