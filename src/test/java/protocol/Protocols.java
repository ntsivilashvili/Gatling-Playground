package protocol;

import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class Protocols {

    public static HttpProtocolBuilder reqResProtocol = HttpDsl.http
            .baseUrl("https://reqres.in")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .userAgentHeader("Gatling Performance Test")
            .disableCaching(); // optional
}
