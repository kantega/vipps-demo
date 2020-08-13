package no.kantega.vippsdemo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.kantega.vipps.HttpClient;
import no.kantega.vipps.dto.GetAccessTokenDTO;
import no.kantega.vippsdemo.config.ISecurityConfig;
import no.kantega.vippsdemo.dto.GrantTokenDTO;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Logger;

import java.io.IOException;

@Service
public class OAuthClientService {

    private HttpClient httpClient;
    private ISecurityConfig securityConfig;

    /** Holds the latest access token */
    private GrantTokenDTO grantTokenDTO;

    Logger logger = Logger.getLogger(OAuthClientService.class.getName());

    @Autowired
    public OAuthClientService(HttpClient httpClient,
                              ISecurityConfig securityConfig) {
        this.httpClient = httpClient;
        this.securityConfig = securityConfig;
    }


    public void createOauthSession(HttpServletRequest req) {
        // Create a new state for this request
        String loginState = UUID.randomUUID().toString();
        req.getSession().setAttribute("loginState", loginState);

        // Specify what scope to request
        String scope = "openid name nnin birthDate email phoneNumber address";

        Request request = new Request.Builder()
                .url(new HttpUrl.Builder()
                        .host(securityConfig.getBaseUrl())
                        .scheme("https")
                        .addPathSegment("/oauth2/auth")
                        .addQueryParameter("reponse_type", "code")
                        .addQueryParameter("scope", scope)
                        .addQueryParameter("state", loginState)
                        .addQueryParameter("redirect_uri", securityConfig.getCallbackPrefix())
                        .build())
                .build();

        try {
            Response response = httpClient.sendRequest(request);

            if (response.isSuccessful()){
                ResponseBody responseBody = response.body();
                if (responseBody != null){
                    String responseBodyString = responseBody.string();
                    logger.info("accesstoken response from Vipps is successful and body is not null");
                    ObjectMapper objectMapper = new ObjectMapper();
                    grantTokenDTO = objectMapper.readValue(responseBodyString, GrantTokenDTO.class);
                }
                else {
                    logger.info("accesstoken response from Vipps is successful and body is null");
                    logger.info("accesstoken response from Vipps=" + response.message());
                }
            }
            else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
