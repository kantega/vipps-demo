package no.kantega.vippsdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements ISecurityConfig {

    @Value("${vipps.client.base_url}")
    private String REDIRECT_URI;

    @Value("${vipps.client.id:client_id}")
    private String CLIENT_ID;

    @Value("${vipps.client.secret:client_secret}")
    private String CLIENT_SECRET;

    @Value("${vipps.login.base_url:apitest.vipps.no}")
    private String VIPPS_API_URL;

    @Value("${vipps.login.access.path:access-management-1.0/access/}")
    private String ACCESS_PATH;

    @Value("${vipps.client.subscription.key}")
    private String ocpApimSubscriptionKey = "e7a0d60fe3e1e7a0d60fe3e14f19977a73b72b5196a54f19977a73b72b5196a5";


    public String getOcpApimSubscriptionKey() {
        return ocpApimSubscriptionKey;
    }

    public String getClientId() {
        return CLIENT_ID;
    }

    public String getClientSecret() {
        return CLIENT_SECRET;
    }

    public String getVippsApiUrl() {
        return VIPPS_API_URL;
    }

    public String getAccessPath() {
        return ACCESS_PATH;
    }

    @Override
    public String getRedirectUri() {
        return REDIRECT_URI;
    }

    public String getCallbackPrefix() {
        return getVippsApiUrl() + "/vipps";
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/**");
    }

}
