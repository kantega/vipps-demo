package no.kantega.vippsdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Oauth2Token {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("token_type")
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
