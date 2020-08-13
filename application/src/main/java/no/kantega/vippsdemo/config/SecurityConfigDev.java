package no.kantega.vippsdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Profile("dev")
public class SecurityConfigDev extends WebSecurityConfigurerAdapter implements ISecurityConfig {

    @Value("${vipps.client.id.test:client_id}")
    private String CLIENT_ID;

    @Value("${vipps.client.secret.test:client_secret}")
    private String CLIENT_SECRET;

    @Value("${vipps.login.base.url.test:https://apitest.vipps.no/access-management-1.0/access/}")
    private String BASE_URL;

    /**
     * @return Vipps Client ID
     */
    public String getClientId() {
        if (CLIENT_ID == null) {
            // A dummy value, from Vipps API doc
            return "fb492b5e-7907-4d83-ba20-c7fb60ca35de";
        }

        return CLIENT_ID;
    }

    /**
     * @return Vipps Client secret
     */
    public String getClientSecret() {
        if (CLIENT_SECRET == null) {
            // A dummy value, from Vipps API doc
            return "Y8Kteew6GE2ZmeycEt6egg==";
        }

        return CLIENT_SECRET;
    }

    /**
     * @return Base URL for this Vipps client
     */
    public String getBaseUrl() {
        if (BASE_URL == null) {
            return "localhost";
        }

        return BASE_URL;
    }

    /**
     * @return Prefix for all callback endpoints
     */
    public String getCallbackPrefix() {
        return getBaseUrl() + "/vipps";
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/login**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().disable();
    }
}
