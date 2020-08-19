package no.kantega.vippsdemo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.kantega.vipps.HttpClient;
import no.kantega.vippsdemo.config.ISecurityConfig;
import no.kantega.vippsdemo.dto.AccessToken;
import no.kantega.vippsdemo.dto.GrantTokenDTO;
import no.kantega.vippsdemo.dto.Oauth2Token;
import no.kantega.vippsdemo.dto.Userinfo;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Logger;

@Service
public class OAuthClientService {

    private HttpClient httpClient;
    private ISecurityConfig securityConfig;

    /**
     * Holds the latest access token
     */
    private GrantTokenDTO grantTokenDTO;
    private AccessToken accessToken;
    private Oauth2Token oauth2Token;
    private Userinfo userinfo;

    Logger logger = Logger.getLogger(OAuthClientService.class.getName());
    private String code;

    @Autowired
    public OAuthClientService(HttpClient httpClient,
                              ISecurityConfig securityConfig) {
        this.httpClient = httpClient;
        this.securityConfig = securityConfig;
    }

    private AccessToken getAccessToken() {
        Request request = new Request.Builder()
                .post(RequestBody.create("", null))
                .url(new HttpUrl.Builder()
                        .scheme("https")
                        .host(securityConfig.getVippsApiUrl())
                        .addPathSegments("accessToken/get")
                        .build()
                )
                .headers(Headers.of(
                        "client_id", securityConfig.getClientId(),
                        "client_secret", securityConfig.getClientSecret(),
                        "Ocp-Apim-Subscription-Key", securityConfig.getOcpApimSubscriptionKey()))
                .build();

        try {
            Response response = httpClient.sendRequest(request);
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (null != body) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    accessToken = objectMapper.readValue(body.string(), AccessToken.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public void redirectToVipps(HttpServletResponse response, String loginState) throws IOException {
        String scope = "openid name nin birthDate email phoneNumber address api_version_2";

        URL authenticationRequest = new HttpUrl.Builder()
                .host(securityConfig.getVippsApiUrl())
                .scheme("https")
                .addPathSegments(securityConfig.getAccessPath())
                .addPathSegments("oauth2/auth")
                .addQueryParameter("response_type", "code")
                .addQueryParameter("client_id", securityConfig.getClientId())
                .addQueryParameter("redirect_uri", securityConfig.getRedirectUri())
                .addQueryParameter("scope", scope)
                .addQueryParameter("state", loginState)
                .build()
                .url();

        response.sendRedirect(authenticationRequest.toString());
    }

    public void setGrantCode(String code) {
        this.code = code;
    }

    protected String createBasicAuthorizationString(String clientId, String clientSecret) {
        String authorizationToken = Base64Utils.encodeToString((clientId + ":" + clientSecret).getBytes());
        return "Basic" + " " + authorizationToken;
    }

    public void getOauthToken() {
        RequestBody formBody = new FormBody.Builder()
                .addEncoded("grant_type", "authorization_code")
                .addEncoded("code", code)
                .addEncoded("redirect_uri", securityConfig.getRedirectUri())
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(new HttpUrl.Builder()
                        .scheme("https")
                        .host(securityConfig.getVippsApiUrl())
                        .addPathSegments(securityConfig.getAccessPath())
                        .addPathSegments("oauth2/token")
                        .build()
                )
                .headers(Headers.of(
                        HttpHeaders.CONTENT_TYPE, securityConfig.getClientId(),
                        HttpHeaders.AUTHORIZATION, createBasicAuthorizationString(securityConfig.getClientId(), securityConfig.getClientSecret())))
                .build();

        try {
            Response response = httpClient.sendRequest(request);
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (null != body) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    oauth2Token = objectMapper.readValue(body.string(), Oauth2Token.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getUserinfo() {
        Request request = new Request.Builder()
                .url(new HttpUrl.Builder()
                        .scheme("https")
                        .host(securityConfig.getVippsApiUrl())
                        .addPathSegments("/vipps-userinfo-api/userinfo")
                        .build()
                )
                .headers(Headers.of(
                        HttpHeaders.AUTHORIZATION, "Bearer " + oauth2Token.getAccessToken()))
                .build();

        try {
            Response response = httpClient.sendRequest(request);
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (null != body) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    userinfo = objectMapper.readValue(body.string(), Userinfo.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
