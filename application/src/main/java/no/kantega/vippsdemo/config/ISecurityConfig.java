package no.kantega.vippsdemo.config;


public interface ISecurityConfig {

    String getClientId();
    String getClientSecret();
    String getVippsApiUrl();
    String getCallbackPrefix();
    String getOcpApimSubscriptionKey();
    String getAccessPath();
    String getRedirectUri();
}