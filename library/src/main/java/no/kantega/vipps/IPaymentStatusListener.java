package no.kantega.vipps;

import no.kantega.vipps.dto.PaymentCallbackInfoDTO;

public interface IPaymentStatusListener {
    /**
     * Set new payment status.
     * @param statusRequest Holds the status to set.
     * @param authToken Holds an optional authentication token for the operation - may be null!
     */
    void setPaymentStatus(String order_id, PaymentCallbackInfoDTO statusRequest, String authToken);
}
