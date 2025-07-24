package api;


import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class ReqResApi {

    /**
     * API key used to authenticate requests to the ReqRes API.
     *
     * This is required by the free version of ReqRes and must be included
     * in every request via the 'x-api-key' header.
     */
    private static final String API_KEY = "reqres-free-v1";

    /**
     * Performs user login via the ReqRes API.
     *
     * This chain does the following:
     * - Checks if the 'authToken' already exists in the Gatling session.
     * - If not present, sends a POST request to /api/login
     *   with the user's 'email' and 'password' from the feeder.
     * - On success, stores the returned token in the session as 'authToken'.
     *
     * This allows subsequent requests to use the token for authentication.
     *
     * Feeder requirements:
     * - 'email' : The user's email address
     * - 'password' : The user's password
     *
     * @return a ChainBuilder that can be used to perform login before protected requests
     */
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

    /**
     * Retrieves a list of users from ReqRes.
     *
     * This is a protected endpoint that requires a valid 'authToken' in the session.
     * The token is included in the 'Authorization' header using the Bearer scheme.
     *
     * @return a ChainBuilder that performs a GET request to /api/users?page=2
     */
    public static ChainBuilder getUsers = exec(
            http("ReqRes Get Users")
                    .get("/api/users?page=2")
                    .header("x-api-key", API_KEY)
                    .header("Authorization", session -> "Bearer " + session.getString("authToken"))
                    .check(status().is(200))
    );
}