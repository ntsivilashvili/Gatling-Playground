package api;

import io.gatling.javaapi.core.ChainBuilder;

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
