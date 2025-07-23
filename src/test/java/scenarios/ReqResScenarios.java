package scenarios;

import api.ReqResApi;
import common.DataFeeder;
import io.gatling.javaapi.core.ScenarioBuilder;

import static io.gatling.javaapi.core.CoreDsl.scenario;

public class ReqResScenarios {

    /**
     * Simulates a login followed by a call to get users from ReqRes API.
     *
     * Steps:
     * 1. Feeds user credentials (email and password) from 'reqres_users.json'.
     * 2. Performs login and stores the token in the session.
     * 3. Sends a GET request to retrieve the list of users using the auth token.
     *
     * Feeder file: data/reqres_users.json
     * Required fields in the feeder:
     * - email (String)
     * - password (String)
     *
     * This scenario mimics a typical authenticated flow.
     */
    public static ScenarioBuilder loginAndGetUsers = scenario("ReqRes Login + Get Users")
            .feed(DataFeeder.getJsonFeeder("/reqres_users.json"))
            .exec(ReqResApi.login)
            .exec(ReqResApi.getUsers);
}
