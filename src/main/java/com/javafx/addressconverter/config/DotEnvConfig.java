package com.javafx.addressconverter.config;


import io.github.cdimascio.dotenv.Dotenv;

public class DotEnvConfig {
    private static Dotenv dotEnv = Dotenv.configure()
            .directory("src/main/resources")
            .filename("com/javafx/addressconverter/env")
            .load();

    public static String getApi() {
        return dotEnv.get("API");
    }

    public static String getApiConfirmKey() {
        return dotEnv.get("API_CONFIRM_KEY");
    }

    public static String getApiResultType() {
        return dotEnv.get("API_RESULT_TYPE");
    }
}
