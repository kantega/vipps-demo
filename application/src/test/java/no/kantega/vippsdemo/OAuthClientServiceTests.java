package no.kantega.vippsdemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import no.kantega.vipps.HttpClient;
import no.kantega.vippsdemo.config.ISecurityConfig;
import no.kantega.vippsdemo.config.SecurityConfig;
import no.kantega.vippsdemo.service.OAuthClientService;

public class OAuthClientServiceTests {

    private OAuthClientService oAuthClientService;
    private HttpClient httpClient = mock(HttpClient.class);
    private ISecurityConfig config = new SecurityConfig();

    public OAuthClientServiceTests() {
        oAuthClientService = new OAuthClientService(httpClient, config);
    }

/*    @Test
    public void getAccessToken() throws IOException {
        Request fakeRequest = new Request.Builder().header("client_Id", "181289716281372")
                .addHeader("client_Secret", "789087608kjhphlo22312").url("http://some.url").build();

        ResponseBody grantBody = ResponseBody.create("{\n" +
                "    \"token_type\": \"Bearer\",\n" +
                "    \"expires_in\": \"3599\",\n" +
                "    \"ext_expires_in\": \"3599\",\n" +
                "    \"expires_on\": \"1597667872\",\n" +
                "    \"not_before\": \"1597663972\",\n" +
                "    \"resource\": \"00000002-0000-0000-c000-000000000000\",\n" +
                "    \"access_token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imh1Tjk1SXZQZmVocTM0R3pCRFoxR1hHaXJuTSIsImtpZCI6Imh1Tjk1SXZQZmVocTM0R3pCRFoxR1hHaXJuTSJ9.eyJhdWQiOiIwMDAwMDAwMi0wMDAwLTAwMDAtYzAwMC0wMDAwMDAwMDAwMDAiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9lNTExNjUyNi01MWRjLTRjMTQtYjA4Ni1hNWNiNDcxNmJjNGIvIiwiaWF0IjoxNTk3NjUzNzA4LCJuYmYiOjE1OTc2NTM3MDgsImV4cCI6MTU5NzY1NzYwOCwiYWlvIjoiRTJCZ1lDZ3RlWEg3M3V1SGRSSXhuMWZOVFhjN0R3QT0iLCJhcHBpZCI6IjhjYWMyNjhmLTE1MGEtNDdiNy1hZjEzLTMwZTcxMmZhNGFkMiIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0L2U1MTE2NTI2LTUxZGMtNGMxNC1iMDg2LWE1Y2I0NzE2YmM0Yi8iLCJ0ZW5hbnRfcmVnaW9uX3Njb3BlIjoiRVUiLCJ0aWQiOiJlNTExNjUyNi01MWRjLTRjMTQtYjA4Ni1hNWNiNDcxNmJjNGIiLCJ1dGkiOiI3eGl0Q01vMlVrcWRMV0hZRzBLVEFBIiwidmVyIjoiMS4wIn0.w1bZ0tEA7TVTIpSanvpxbmS8JInaet9tXPoe2_AxkUBe8E1pn4KI4VV9NxQ6Tfun9Rn3bIh0P4cKiJ-M4zrqbrey81ysw55k6Gx6hwBM3wNRrynEgZ2vVYpT0vDdM3LE6panCejwYCipTe8PlxFnfjSQCn4p_gO9vtpRXE-Ds_kaa1jARJaunZetFRHryAIhEpSBJeTirFfQP0uDrNXXY0PH-HjsZSKN82SJdVk5HzGnwljEMMQwVQmDGdJhq2bSDUSeMSKgoVFwsw8Oxeiyu0rucO-yDEYLkxzzlPXxX8tz_zRukQjV3MVjJ-2_78wRaXFulx8BZflHuWK6jQ18yg\"\n" +
                "}", MediaType.get("application/json"));

        Response fakeGrantResponse = new Response.Builder().addHeader("content-type", "application/json; charset=utf-8")
                .code(200).protocol(Protocol.HTTP_2).message("Unauthorized").request(fakeRequest).body(grantBody)
                .build();
        when(httpClient.sendRequest(any())).thenReturn(fakeGrantResponse);

        AccessToken accessToken = oAuthClientService.getAccessToken();

        assertEquals("Bearer", accessToken.getTokenType());
        assertEquals(3599, accessToken.getExpiresIn());
        assertEquals(3599, accessToken.getExtExpiresIn());
        assertEquals(1597667872, accessToken.getExpiresOn());
        assertEquals(1597663972, accessToken.getNotBefore());
        assertEquals("00000002-0000-0000-c000-000000000000", accessToken.getResource());
        assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Imh1Tjk1SXZQZmVocTM0R3pCRFoxR1hHaXJuTSIsImtpZCI6Imh1Tjk1SXZQZmVocTM0R3pCRFoxR1hHaXJuTSJ9.eyJhdWQiOiIwMDAwMDAwMi0wMDAwLTAwMDAtYzAwMC0wMDAwMDAwMDAwMDAiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9lNTExNjUyNi01MWRjLTRjMTQtYjA4Ni1hNWNiNDcxNmJjNGIvIiwiaWF0IjoxNTk3NjUzNzA4LCJuYmYiOjE1OTc2NTM3MDgsImV4cCI6MTU5NzY1NzYwOCwiYWlvIjoiRTJCZ1lDZ3RlWEg3M3V1SGRSSXhuMWZOVFhjN0R3QT0iLCJhcHBpZCI6IjhjYWMyNjhmLTE1MGEtNDdiNy1hZjEzLTMwZTcxMmZhNGFkMiIsImFwcGlkYWNyIjoiMSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0L2U1MTE2NTI2LTUxZGMtNGMxNC1iMDg2LWE1Y2I0NzE2YmM0Yi8iLCJ0ZW5hbnRfcmVnaW9uX3Njb3BlIjoiRVUiLCJ0aWQiOiJlNTExNjUyNi01MWRjLTRjMTQtYjA4Ni1hNWNiNDcxNmJjNGIiLCJ1dGkiOiI3eGl0Q01vMlVrcWRMV0hZRzBLVEFBIiwidmVyIjoiMS4wIn0.w1bZ0tEA7TVTIpSanvpxbmS8JInaet9tXPoe2_AxkUBe8E1pn4KI4VV9NxQ6Tfun9Rn3bIh0P4cKiJ-M4zrqbrey81ysw55k6Gx6hwBM3wNRrynEgZ2vVYpT0vDdM3LE6panCejwYCipTe8PlxFnfjSQCn4p_gO9vtpRXE-Ds_kaa1jARJaunZetFRHryAIhEpSBJeTirFfQP0uDrNXXY0PH-HjsZSKN82SJdVk5HzGnwljEMMQwVQmDGdJhq2bSDUSeMSKgoVFwsw8Oxeiyu0rucO-yDEYLkxzzlPXxX8tz_zRukQjV3MVjJ-2_78wRaXFulx8BZflHuWK6jQ18yg", accessToken.getAccessToken());

    }*/
/*
    @Test
    public void createClientSessionSuccess() throws IOException {

        // Create a fake request which is required for to build a fake response
        Request fakeRequest = new Request.Builder().header("client_Id", "181289716281372")
                .addHeader("client_Secret", "789087608kjhphlo22312").url("http://some.url").build();

        ResponseBody grantBody = ResponseBody.create("{}", MediaType.get("application/json"));

        // Create a fake response with a random (not success) message
        Response fakeGrantResponse = new Response.Builder().addHeader("content-type", "application/json; charset=utf-8")
                .code(401).protocol(Protocol.HTTP_2).message("Unauthorized").request(fakeRequest).body(grantBody)
                .build();

        when(httpClient.sendRequest(any())).thenReturn(fakeGrantResponse);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(req.getSession()).thenReturn(session);

        oAuthClientService.redirectToVipps(req);

        // Check that we do make a call to the correct end point
        ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
        verify(httpClient, times(1)).sendRequest(argument.capture());
        assertTrue(argument.getValue().url().toString()
                .contains(URLEncoder.encode("/oauth2/auth", StandardCharsets.UTF_8.toString())));
    }*/
}
