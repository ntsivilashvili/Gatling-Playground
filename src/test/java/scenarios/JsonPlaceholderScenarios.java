package scenarios;

import static io.gatling.javaapi.core.CoreDsl.*;
import api.JsonPlaceholderApi;
import common.DataFeeder;
import io.gatling.javaapi.core.ScenarioBuilder;

public class JsonPlaceholderScenarios {

    /**
     * Full CRUD flow using JSONPlaceholder.
     *
     * This scenario performs a complete sequence:
     * 1. GET all posts
     * 2. GET a post by ID (from feeder)
     * 3. POST to create a new post
     * 4. PUT to update that post
     * 5. DELETE the post
     *
     * Includes 1-second pauses between each step to simulate real user behavior.
     *
     * Feeder file: data/posts.json (randomized)
     */
    public static ScenarioBuilder fullCrudFlow = scenario("Full CRUD with JSONPlaceholder")
            .feed(DataFeeder.getJsonFeeder("posts.json").random())
            .exec(JsonPlaceholderApi.getPosts)
            .pause(1)
            .exec(JsonPlaceholderApi.getPostById)
            .pause(1)
            .exec(JsonPlaceholderApi.createPost)
            .pause(1)
            .exec(JsonPlaceholderApi.updatePost)
            .pause(1)
            .exec(JsonPlaceholderApi.deletePost);

    /**
     * Repeats the create post operation 3 times using random data each time.
     *
     * This demonstrates how Gatling handles repeated execution
     * with randomized feeder values.
     *
     * Useful for testing multiple variations of input.
     */
    public static ScenarioBuilder repeatWithRandomData = scenario("Repeat 3 times with Random Data")
            .repeat(3).on(
                    feed(DataFeeder.getJsonFeeder("posts.json").random())
                            .exec(JsonPlaceholderApi.createPost)
            );

    public static ScenarioBuilder repeatWithRandomDataClosed = scenario("Repeat with Random Data - Closed")
            .repeat(3).on(
                    feed(DataFeeder.getJsonFeeder("posts.json").random())
                            .exec(JsonPlaceholderApi.createPost)
            );

    /**
     * Uses a circular feeder with 3 users defined in the JSON file.
     *
     * Each virtual user will get the next available data row from the JSON,
     * and loop back to the beginning if needed.
     *
     * This setup ensures even data distribution among users.
     */
    public static ScenarioBuilder threeUsersCircular = scenario("3 Users Circular Feeder")
            .feed(DataFeeder.getJsonFeeder("posts.json").circular())
            .exec(JsonPlaceholderApi.createPost);

    /**
     * Each of the 3 users runs the create post operation 3 times.
     *
     * On every iteration, the feeder provides a new random set of data.
     * This simulates a user performing the same task multiple times with different input.
     */
    public static ScenarioBuilder threeUsersThreeIterations = scenario("3 Users × 3 Iterations Each")
            .repeat(3).on(
                    feed(DataFeeder.getJsonFeeder("posts.json").random())
                            .exec(JsonPlaceholderApi.createPost)
            );

    /**
     * Scenario demonstrating different user actions with weighted probabilities.
     *
     * Users randomly perform one of the following actions per iteration:
     *  - 70% chance to create a new post
     *  - 20% chance to update an existing post
     *  - 10% chance to delete a post
     *
     * This simulates realistic user behavior with varied traffic patterns,
     * useful for testing how the system handles different operations under load.
     *
     * Feeder:
     *  - Uses randomized data from posts.json feeder to supply required fields.
     */
    public static ScenarioBuilder randomUserActions = scenario("Random User Actions with Probabilities")
            .feed(DataFeeder.getJsonFeeder("posts.json").random())
            .randomSwitch().on(
                    percent(70.0).then(exec(JsonPlaceholderApi.createPost)),
                    percent(20.0).then(exec(JsonPlaceholderApi.updatePost)),
                    percent(10.0).then(exec(JsonPlaceholderApi.deletePost))
            );
}