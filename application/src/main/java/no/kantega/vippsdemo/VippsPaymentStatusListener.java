package no.kantega.vippsdemo;

import no.kantega.vipps.IPaymentStatusListener;
import no.kantega.vipps.dto.PaymentCallbackInfoDTO;
import no.kantega.vippsdemo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Implements a listener on Vipps payment status updates.
 */
@Component
public class VippsPaymentStatusListener implements IPaymentStatusListener {

    private PaymentService paymentService;

    Logger logger = Logger.getLogger(VippsPaymentStatusListener.class.getName());

    @Autowired
    public VippsPaymentStatusListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Set payment status based on input.
     * @param statusRequest Holds information about the new payment status.
     */
    @Override
    public void setPaymentStatus(String order_id, PaymentCallbackInfoDTO statusRequest, String authToken) {
        logger.info("Received request to set payment status for order: " + order_id);

        String status = statusRequest.getTransactionInfo().getStatus().toString();
        logger.info("New order status is: " + status);

        paymentService.updateOrderStatus(order_id, status, authToken);
    }
}
