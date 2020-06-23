package no.kantega.vipps.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class VippsClientConfigDev implements IVippsClientConfig {

    @Value("${vipps.client.id.test:client_id}")
    private String CLIENT_ID;

    @Value("${vipps.client.secret.test:client_secret}")
    private String CLIENT_SECRET;

    @Value("${vipps.ocp.apim.subscription.key.ecommerce.test:subscription_key}")
    private String OCP_APIM_SUBSCRIPTION_KEY_ECOMMERCE;

    @Value("${vipps.base.url.test:localhost}")
    private String BASE_URL;

    @Value("${vipps.fallback.url.test:localhost/complete.html}")
    private String FALLBACK_URL;

    @Value("${vipps.merchant.serial.no.test:123456789}")
    private String MERCHANT_SERIAL_NO;

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
     * @return Subscription key for the ecom API
     */
    public String getEcomSubscriptionKey() {
        if (OCP_APIM_SUBSCRIPTION_KEY_ECOMMERCE == null) {
            // A dummy value, from Vipps API doc
            return "0f14ebcab0ec4b29ae0cb90d91b4a84a";
        }

        return OCP_APIM_SUBSCRIPTION_KEY_ECOMMERCE;
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

    /**
     * @return Fallback url when returning from payment
     */
    public String getFallbackUrl() {
        if (FALLBACK_URL == null) {
            return "https://example.com";
        }

        return FALLBACK_URL;
    }

    /**
     * @return Merchant serial number
     */
    public String getMerchantSerialNo() {
        if (MERCHANT_SERIAL_NO == null) {
            // A dummy value, from Vipps API doc
            return "123456";
        }

        return MERCHANT_SERIAL_NO;
    }
}
