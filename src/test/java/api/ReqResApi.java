package api;


import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ReqResApi {
    private static final String API_KEY = "reqres-free-v1";

    public static ChainBuilder login = exec(session -> {
        if (session.contains("authToken") && !session.getString("authToken").isEmpty()) {
            return session;
        }
        return session;
    })
            .doIf(session -> !session.contains("authToken"))
            .then(
                    exec(
                            http("ReqRes Login")
                                    .post("/api/login")
                                    .header("x-api-key", API_KEY)
                                    .body(StringBody(session ->
                                            "{ \"email\": \"" + session.getString("email") + "\", " +
                                                    "\"password\": \"" + session.getString("password") + "\" }"
                                    ))
                                    .check(status().is(200))
                                    .check(jsonPath("$.token").saveAs("authToken"))
                    )
            );

    public static ChainBuilder getUsers = exec(
            http("ReqRes Get Users")
                    .get("/api/users?page=2")
                    .header("x-api-key", API_KEY)
                    .header("Authorization", session -> "Bearer " + session.getString("authToken"))
                    .check(status().is(200))
    );
}
