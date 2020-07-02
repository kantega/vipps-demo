package no.kantega.vippsdemo;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String WHITELISTED_IPS =
            "hasIpAddress('172.16.25.0/24') " + // Tests servers
            "or hasIpAddress('51.105.193.242')" + // Vipps test servers
            "";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.csrf().disable().authorizeRequests().antMatchers("/vipps/v2/**").access(WHITELISTED_IPS);
        http.csrf().disable().authorizeRequests().antMatchers("/vipps/v2/**").permitAll();
    }
}
