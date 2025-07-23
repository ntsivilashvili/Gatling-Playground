package simulations;

import static io.gatling.javaapi.core.CoreDsl.*;
import io.gatling.javaapi.core.Simulation;
import scenarios.ReqResScenarios;
import protocol.Protocols;

import java.time.Duration;

public class ReqResSimulation extends Simulation {

    {
        setUp(
                ReqResScenarios.loginAndGetUsers.injectOpen(
                        rampUsers(3).during(Duration.ofSeconds(10))
                ).protocols(Protocols.reqResProtocol)
        );
    }
}
