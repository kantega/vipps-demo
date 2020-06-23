package no.kantega.vipps.config;


public interface IVippsClientConfig {

    String getClientId() throws NullPointerException;
    String getClientSecret() throws NullPointerException;
    String getEcomSubscriptionKey() throws NullPointerException;
    String getBaseUrl() throws NullPointerException;
    String getCallbackPrefix() throws NullPointerException;
    String getFallbackUrl() throws NullPointerException;
    String getMerchantSerialNo() throws NullPointerException;
}
