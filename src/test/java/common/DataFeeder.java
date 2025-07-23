package common;

import io.gatling.javaapi.core.FeederBuilder;

import static io.gatling.javaapi.core.CoreDsl.jsonFile;

public class DataFeeder {

    public static FeederBuilder.FileBased<Object> getJsonFeeder(String fileName){
        return jsonFile("data/" + fileName).circular();
    }
}
