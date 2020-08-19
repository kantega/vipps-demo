package no.kantega.vippsdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessToken {

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Number expiresIn;

    @JsonProperty("ext_expires_in")
    private Number extExpiresIn;

    @JsonProperty("expires_on")
    private Long expiresOn;

    @JsonProperty("not_before")
    private Long notBefore;

    private String resource;

    @JsonProperty("access_token")
    private String accessToken;


    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Number getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Number expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Number getExtExpiresIn() {
        return extExpiresIn;
    }

    public void setExtExpiresIn(Number extExpiresIn) {
        this.extExpiresIn = extExpiresIn;
    }

    public Long getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Long expiresOn) {
        this.expiresOn = expiresOn;
    }

    public Long getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Long notBefore) {
        this.notBefore = notBefore;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
