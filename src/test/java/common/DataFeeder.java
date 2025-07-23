package common;

import io.gatling.javaapi.core.FeederBuilder;

import static io.gatling.javaapi.core.CoreDsl.jsonFile;

public class DataFeeder {

    /**
     * Loads a JSON file from the 'data' directory and returns it as a circular feeder.
     *
     * Circular means that when Gatling reaches the end of the file, it loops back to the beginning.
     * This ensures that data is always available, even if the number of users exceeds the number of entries in the file.
     *
     * Example:
     * - fileName = "users.json" will load "data/users.json"
     *
     * @param fileName The name of the JSON file to load (relative to the 'data' folder)
     * @return A circular FeederBuilder that can be used in scenarios
     */
    public static FeederBuilder.FileBased<Object> getJsonFeeder(String fileName){
        return jsonFile("data/" + fileName).circular();
    }
}
