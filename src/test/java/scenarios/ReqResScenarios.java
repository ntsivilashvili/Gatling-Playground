package scenarios;

import api.ReqResApi;
import common.DataFeeder;
import io.gatling.javaapi.core.ScenarioBuilder;

import static io.gatling.javaapi.core.CoreDsl.scenario;

public class ReqResScenarios {
    public static ScenarioBuilder loginAndGetUsers = scenario("ReqRes Login + Get Users")
            .feed(DataFeeder.getJsonFeeder("/reqres_users.json"))
            .exec(ReqResApi.login)
            .exec(ReqResApi.getUsers);
}
