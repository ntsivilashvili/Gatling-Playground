package protocol;

import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class Protocols {

    /**
     * HTTP protocol configuration for ReqRes API.
     *
     * This sets up:
     * - Base URL: https://reqres.in
     * - Accept and Content-Type headers as application/json
     * - A custom User-Agent header for identification
     * - Caching disabled (optional)
     *
     * Use this when testing endpoints from the ReqRes API.
     */
    public static HttpProtocolBuilder reqResProtocol = HttpDsl.http
            .baseUrl("https://reqres.in")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .userAgentHeader("Gatling Performance Test")
            .disableCaching(); // optional

    /**
     * HTTP protocol configuration for JSONPlaceholder API.
     *
     * This sets up:
     * - Base URL: https://jsonplaceholder.typicode.com
     * - Accept and Content-Type headers as application/json
     *
     * Use this when testing endpoints from the JSONPlaceholder API.
     */
    public static HttpProtocolBuilder jsonPlaceholderProtocol = HttpDsl.http
            .baseUrl("https://jsonplaceholder.typicode.com")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");
}