package io.github.drspeedy.noticonnectandroid;

/**
 * Created by doc on 11/6/16.
 *
 * Singleton dependency container
 */

public class Application {
    private static Application sApplication;

    public static final String BASE_HOST = "http://172.16.1.36:8000";
    public static final String API_HOST = BASE_HOST + "/api";

    public static Application getInstance() {
        if (sApplication == null) {
            sApplication = new Application();
            return sApplication;
        } else {
            return sApplication;
        }
    }
}
