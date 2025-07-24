package simulations;

import static io.gatling.javaapi.core.CoreDsl.*;
import io.gatling.javaapi.core.Simulation;
import scenarios.JsonPlaceholderScenarios;
import protocol.Protocols;

import java.time.Duration;

public class JsonPlaceholderSimulation extends Simulation {

    /**
     * Simulation for testing various flows using the JSONPlaceholder API.
     *
     * This class showcases multiple realistic user behaviors using different feeder strategies and injection profiles:
     *
     * Scenarios:
     *
     * 1. fullCrudFlow:
     *    - Complete GET, POST, PUT, DELETE sequence using random data
     *    - 3 users ramping up over 10 seconds
     *
     * 2. repeatWithRandomData:
     *    - One user performing three repeated POST requests with random data
     *    - Injected at once
     *
     * 3. repeatWithRandomDataClosed (concurrent):
     *    - Two users constantly sending repeated POSTs over 15 seconds
     *    - Tests concurrency behavior
     *
     * 4. threeUsersCircular:
     *    - Three users each sending a single POST request using circular feeder
     *    - Ensures unique data distribution per user
     *
     * 5. threeUsersThreeIterations:
     *    - Three users each performing 3 POST operations using random data
     *    - Simulates repetitive actions from each user
     *
     * 6. randomUserActions:
     *    - Five users ramping up over 10 seconds
     *    - Each user randomly performs one of the following per iteration:
     *       • 70% chance to create a new post
     *       • 20% chance to update an existing post
     *       • 10% chance to delete a post
     *    - Demonstrates weighted random user behavior simulation
     *
     * Assertions:
     * - Global maximum response time must be below 1500 ms
     * - Global failure rate must be less than 5%
     */
    {
        setUp(
                // Original full CRUD scenario with ramped users
                JsonPlaceholderScenarios.fullCrudFlow.injectOpen(
                        rampUsers(3).during(Duration.ofSeconds(10))
                ).protocols(Protocols.jsonPlaceholderProtocol),

                // One user performing 3 POST requests with random data at once
                JsonPlaceholderScenarios.repeatWithRandomData.injectOpen(
                        atOnceUsers(1)
                ).protocols(Protocols.jsonPlaceholderProtocol),

                // Two concurrent users constantly sending repeated POSTs over 15 seconds
                JsonPlaceholderScenarios.repeatWithRandomDataClosed.injectClosed(
                        constantConcurrentUsers(2).during(Duration.ofSeconds(15))
                ).protocols(Protocols.jsonPlaceholderProtocol),

                // Three users each sending one POST with circular feeder data
                JsonPlaceholderScenarios.threeUsersCircular.injectOpen(
                        atOnceUsers(3)
                ).protocols(Protocols.jsonPlaceholderProtocol),

                // Three users each performing 3 POST operations using random data
                JsonPlaceholderScenarios.threeUsersThreeIterations.injectOpen(
                        atOnceUsers(3)
                ).protocols(Protocols.jsonPlaceholderProtocol),

                // Five users ramping up over 10 seconds, randomly choosing actions with weighted probabilities
                JsonPlaceholderScenarios.randomUserActions.injectOpen(
                        rampUsers(5).during(Duration.ofSeconds(10))
                ).protocols(Protocols.jsonPlaceholderProtocol)
        ).assertions(
                global().responseTime().max().lt(1500), // Max 1500ms response time
                global().failedRequests().percent().lt(5.0) // <5% failure rate
        );
    }
}