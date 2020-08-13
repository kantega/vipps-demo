package no.kantega.vippsdemo;

import no.kantega.vipps.HttpClient;
import no.kantega.vippsdemo.config.ISecurityConfig;
import no.kantega.vippsdemo.config.SecurityConfigDev;
import no.kantega.vippsdemo.service.OAuthClientService;
import okhttp3.*;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class OAuthClientServiceTests {

    private OAuthClientService oAuthClientService;
    private HttpClient httpClient = mock(HttpClient.class);
    private ISecurityConfig config = new SecurityConfigDev();

    public OAuthClientServiceTests() {
        oAuthClientService = new OAuthClientService(httpClient, config);
    }

    @Test
    public void createClientSessionSuccess() throws IOException {

        // Create a fake request which is required for to build a fake response
        Request fakeRequest = new Request.Builder()
                    .header("client_Id", "181289716281372")
                    .addHeader("client_Secret", "789087608kjhphlo22312")
                    .url("http://some.url")
                    .build();

        ResponseBody grantBody = ResponseBody.create("{}", MediaType.get("application/json"));

        // Create a fake response with a random (not success) message
        Response fakeGrantResponse = new Response.Builder()
                .addHeader("content-type","application/json; charset=utf-8")
                .code(401)
                .protocol(Protocol.HTTP_2)
                .message("Unauthorized")
                .request(fakeRequest)
                .body(grantBody)
                .build();

        when(httpClient.sendRequest(any())).thenReturn(fakeGrantResponse);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(req.getSession()).thenReturn(session);

        oAuthClientService.createOauthSession(req);

        // Check that we do make a call to the correct end point
        ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
        verify(httpClient, times(1)).sendRequest(argument.capture());
        assertTrue(argument.getValue().url().toString().contains(URLEncoder.encode("/oauth2/auth", StandardCharsets.UTF_8.toString())));
    }
}
