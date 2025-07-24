package api;

import io.gatling.javaapi.core.ChainBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class JsonPlaceholderApi {

    /**
     * Retrieves all posts.
     *
     * Sends a GET request to /posts endpoint.
     * Checks that the response status code is 200 (OK).
     *
     * @return a ChainBuilder that performs the GET /posts request
     */
    public static ChainBuilder getPosts = exec(
            http("[GET] All Posts")
                    .get("/posts")
                    .check(status().is(200))
    );

    /**
     * Attempts to retrieve all posts, retrying up to 3 times if the request fails.
     *
     * This block wraps the GET /posts call inside a tryMax structure, allowing up to 3 retries
     * in case of temporary failures (like timeouts or 5xx responses).
     * A short pause of 1 second is added between retries to avoid overwhelming the server.
     *
     * Checks that the response status is 200 (OK).
     *
     * @return a ChainBuilder that performs the GET /posts request with retry logic
     */
    public static ChainBuilder getPostsWithRetry = tryMax(3).on(
            exec(
                    http("[GET] All Posts (with Retry)")
                            .get("/posts")
                            .check(status().is(200))
            )
                    .pause(1) // Pause 1 second between attempts
                    .exitHereIfFailed() // exits the scenario early if all retries failed
    );

    /**
     * Retrieves a single post by its ID.
     *
     * Uses the "id" value stored in the Gatling session to build the URL.
     * Sends a GET request to /posts/{id}.
     * Checks that the response status code is 200 (OK).
     *
     * @return a ChainBuilder that performs the GET /posts/{id} request
     */
    public static ChainBuilder getPostById = exec(
            http("[GET] Post by ID")
                    .get(session -> "/posts/" + session.get("id"))
                    .check(status().is(200))
    );

    /**
     * Creates a new post.
     *
     * Sends a POST request to /posts with a JSON body.
     * The body includes "title", "body", and "userId" values taken from the session.
     * Checks that the response status is 201 (Created) or 200 (OK).
     * Saves the newly created post's ID as "newPostId" in the session.
     *
     * Feeder/session requirements:
     * - title (String)
     * - body (String)
     * - userId (int)
     *
     * @return a ChainBuilder that performs the POST /posts request
     */
    public static ChainBuilder createPost = exec(
            http("[POST] Create Post")
                    .post("/posts")
                    .body(StringBody(session -> "{\n" +
                            "  \"title\": \"" + session.getString("title") + "\",\n" +
                            "  \"body\": \"" + session.getString("body") + "\",\n" +
                            "  \"userId\": " + session.getInt("userId") + "\n" +
                            "}"))
                    .asJson()
                    .check(status().in(201, 200)) // placeholder returns 201
                    .check(jsonPath("$.id").saveAs("newPostId"))
    );

    /**
     * Creates a new post using a JSON payload loaded from a file,
     * with Gatling EL placeholders processed (e.g., #{title}, #{body}, #{userId}).
     *
     * Use this when your payload contains dynamic variables that need to be replaced
     * from the Gatling session during runtime.
     *
     * The JSON file should be located at: resources/data/posts.json
     *
     * Difference from Data Feeder:
     * - Data feeders provide session variables (like title, body, userId).
     * - ElFileBody uses those variables to dynamically fill placeholders in the JSON payload.
     * - Feeder feeds data into the session; ElFileBody injects that data into the request body.
     */
    public static ChainBuilder createPostElFileBody = exec(
            http("[POST] Create Post with ElFileBody")
                    .post("/posts")
                    .body(ElFileBody("data/posts.json")) // EL placeholders are resolved at runtime
                    .asJson()
                    .check(status().in(201, 200))
                    .check(jsonPath("$.id").saveAs("newPostId"))
    );

    /**
     * Creates a new post by sending a static JSON payload directly from a file,
     * without any Gatling EL placeholder processing.
     *
     * Use this when you want to send the exact same payload every time,
     * with no dynamic substitution.
     *
     * The JSON file should be located at: resources/data/posts.json
     *
     * Difference from Data Feeder:
     * - No session data or dynamic variables are used here.
     * - The request body is fixed and always identical.
     * - Data feeders are not needed because there's no variable input.
     */
    public static ChainBuilder createPostRawFileBody = exec(
            http("[POST] Create Post with RawFileBody")
                    .post("/posts")
                    .body(RawFileBody("data/posts.json")) // No EL processing, file content sent as-is
                    .asJson()
                    .check(status().in(201, 200))
                    .check(jsonPath("$.id").saveAs("newPostId"))
    );

    /**
     * Creates a new post by streaming the JSON payload from an InputStream.
     *
     * Use this when sending very large payloads that might not fit comfortably
     * in memory, or when you want to control streaming.
     *
     * This method does NOT process Gatling EL placeholders;
     * the payload is sent exactly as in the file.
     *
     * The JSON file path is relative or absolute in the code,
     * e.g., "src/test/resources/data/posts.json"
     *
     * Difference from Data Feeder:
     * - Similar to RawFileBody, it sends a fixed payload.
     * - No dynamic data substitution from session variables.
     * - Data feeders are unnecessary since no variable data is injected.
     */
    public static ChainBuilder createPostInputStreamBody = exec(
            http("[POST] Create Post with InputStreamBody")
                    .post("/posts")
                    .body(InputStreamBody(session -> {
                        try {
                            return new FileInputStream("src/test/resources/data/posts.json");
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }))
                    .asJson()
                    .check(status().in(201, 200))
                    .check(jsonPath("$.id").saveAs("newPostId"))
    );

    /**
     * Updates an existing post by ID.
     *
     * Sends a PUT request to /posts/{id} with a JSON body.
     * The body includes "id", "title", "body", and "userId" from the session.
     * Checks that the response status is 200 (OK).
     *
     * Feeder/session requirements:
     * - id (int or string)
     * - title (String)
     * - body (String)
     * - userId (int)
     *
     * @return a ChainBuilder that performs the PUT /posts/{id} request
     */
    public static ChainBuilder updatePost = exec(
            http("[PUT] Update Post")
                    .put(session -> "/posts/" + session.get("id"))
                    .body(StringBody(session -> "{\n" +
                            "  \"id\": " + session.get("id") + ",\n" +
                            "  \"title\": \"" + session.getString("title") + "\",\n" +
                            "  \"body\": \"" + session.getString("body") + "\",\n" +
                            "  \"userId\": " + session.get("userId") + "\n" +
                            "}"))
                    .asJson()
                    .check(status().is(200))
    );

    /**
     * Deletes a post by ID.
     *
     * Sends a DELETE request to /posts/{id}.
     * Checks that the response status is either 200 (OK) or 204 (No Content).
     *
     * Feeder/session requirement:
     * - id (int or string)
     *
     * @return a ChainBuilder that performs the DELETE /posts/{id} request
     */
    public static ChainBuilder deletePost = exec(
            http("[DELETE] Delete Post")
                    .delete(session -> "/posts/" + session.get("id"))
                    .check(status().in(200, 204))
    );
}