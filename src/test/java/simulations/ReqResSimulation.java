package simulations;

import static io.gatling.javaapi.core.CoreDsl.*;
import io.gatling.javaapi.core.Simulation;
import scenarios.ReqResScenarios;
import protocol.Protocols;

import java.time.Duration;

public class ReqResSimulation extends Simulation {

    /**
     * Simulation for testing the ReqRes login and user retrieval flow.
     *
     * This scenario represents a simple authenticated user journey:
     * 1. Reads email and password from JSON feeder (`reqres_users.json`)
     * 2. Logs in using ReqRes API and stores the auth token in session
     * 3. Fetches the user list using the token
     *
     * Injection Profile:
     * - 3 users ramping up over 10 seconds
     *
     * Protocol:
     * - Uses the configured ReqRes HTTP protocol
     *
     * Assertions:
     * - Max response time must be under 1000 ms
     * - Failure rate must be less than 3%
     */
    {
        setUp(
                ReqResScenarios.loginAndGetUsers.injectOpen(
                        rampUsers(3).during(Duration.ofSeconds(10))
                ).protocols(Protocols.reqResProtocol)
        )
                .assertions(
                        global().responseTime().max().lt(1000),
                        global().failedRequests().percent().lt(3.0)
                );
    }
}
