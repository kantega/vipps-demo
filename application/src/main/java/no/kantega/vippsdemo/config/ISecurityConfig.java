package no.kantega.vippsdemo.config;


public interface ISecurityConfig {

    String getClientId() throws NullPointerException;
    String getClientSecret() throws NullPointerException;
    String getBaseUrl() throws NullPointerException;
    String getCallbackPrefix() throws NullPointerException;
}