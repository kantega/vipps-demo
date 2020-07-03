package no.kantega.vippsdemo;

import no.kantega.vipps.IConsentStatusListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Implements a listener on Vipps consent status updates.
 */
@Component
public class VippsConsentStatusListener implements IConsentStatusListener {

    Logger logger = Logger.getLogger(VippsPaymentStatusListener.class.getName());

    @Override
    public void setConsentStatus(String user_id, String consentKey, boolean consentValue) {

    }
}
