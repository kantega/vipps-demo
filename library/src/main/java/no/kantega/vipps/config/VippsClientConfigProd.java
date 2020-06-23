package no.kantega.vipps.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Configuration
@Profile("prod")
public class VippsClientConfigProd implements IVippsClientConfig {

    @Value("${vipps.client.id}")
    private String CLIENT_ID;

    @Value("${vipps.client.secret}")
    private String CLIENT_SECRET;

    @Value("${vipps.ocp.apim.subscription.key.ecommerce}")
    private String OCP_APIM_SUBSCRIPTION_KEY_ECOMMERCE;

    @Value("${vipps.base.url}")
    private String BASE_URL;

    @Value("${vipps.fallback.url}")
    private String FALLBACK_URL;

    @Value("${vipps.merchant.serial.no}")
    private String MERCHANT_SERIAL_NO;

    /**
     * Returns true if the provided url is valid, otherwise false.
     */
    private boolean urlIsValid(String url) {
        try {
            URL urlValidator = new URL(url);
            urlValidator.toURI();
        }
        catch (MalformedURLException | URISyntaxException e) {
            // The url cannot be converted due to malformed or invalid syntax
            return false;
        }

        return true;
    }

    /**
     * @return Vipps Client ID
     * @throws NullPointerException if no client ID is found
     */
    public String getClientId() throws NullPointerException {
        if (CLIENT_ID == null) {
            throw new NullPointerException("No client ID found!");
        }

        return CLIENT_ID;
    }

    /**
     * @return Vipps Client secret
     */
    public String getClientSecret() throws NullPointerException {
        if (CLIENT_SECRET == null) {
            throw new NullPointerException("No client secret found!");
        }

        return CLIENT_SECRET;
    }

    /**
     * @return Subscription key for the ecom API
     */
    public String getEcomSubscriptionKey() throws NullPointerException {
        if (OCP_APIM_SUBSCRIPTION_KEY_ECOMMERCE == null) {
            throw new NullPointerException("No subscription key found!");
        }

        return OCP_APIM_SUBSCRIPTION_KEY_ECOMMERCE;
    }

    /**
     * @return Base URL for this Vipps client
     */
    public String getBaseUrl() throws NullPointerException {
        if (BASE_URL == null || urlIsValid(BASE_URL)) {
            throw new NullPointerException("No base url key found!");
        }

        return BASE_URL;
    }

    /**
     * @return Prefix for all callback endpoints
     */
    public String getCallbackPrefix() throws NullPointerException{
        return getBaseUrl() + "/vipps";
    }

    /**
     * @return Fallback url for completing payment
     */
    public String getFallbackUrl() throws NullPointerException {
        if (FALLBACK_URL == null || urlIsValid(FALLBACK_URL)) {
            throw new NullPointerException("No fallback url found!");
        }

        return FALLBACK_URL;
    }

    /**
     * @return Merchant serial number
     */
    public String getMerchantSerialNo() throws NullPointerException {
        if (MERCHANT_SERIAL_NO == null) {
            throw new NullPointerException("No merchant serial number found!");
        }

        return MERCHANT_SERIAL_NO;
    }
}
