package no.kantega.vipps;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * This is a wrapper for the OkHttpClient,
 * to be able to separate calls to a http server for easy mocking.
 * <p>
 * Use this whenever you want to fake a server response.
 */
public class HttpClient {

    private final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .followRedirects(false)
            .followSslRedirects(false)
            .build();

    public Response sendRequest(Request request) throws IOException {
        return okHttpClient.newCall(request).execute();
    }
}
