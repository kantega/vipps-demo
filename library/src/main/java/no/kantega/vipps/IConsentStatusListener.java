package no.kantega.vipps;


public interface IConsentStatusListener {
    /**
     * Set new consent status.
     * The consent is identified by a key (if more than one consents),
     * while the consentValue indicates whether the end user has consented or not.
     *
     * @param user_id Identifies the end user on this system.
     * @param consentKey Holds the consent key/name - may be null!
     * @param consentValue Holds the status of the consent.
     */
    void setConsentStatus(String user_id, String consentKey, boolean consentValue);
}
