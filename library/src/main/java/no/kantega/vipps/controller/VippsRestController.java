package no.kantega.vipps.controller;

import no.kantega.vipps.IPaymentStatusListener;
import no.kantega.vipps.dto.PaymentCallbackInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

/**
 * The Vipps REST controller provides the necessary endpoints to meet the requirements
 * for a successful payment service integration, including callbacks, a refund service
 * order details information, and consent withdrawal.
 */
@CrossOrigin
@RequestMapping("/vipps/v2")
@RestController
public class VippsRestController {

    // Holds a listener object for payment status callbacks
    private IPaymentStatusListener paymentStatusListener;

    Logger logger = Logger.getLogger(VippsRestController.class.getName());

    /**
     * Call this method to register a listener for payment status updates
     * @param listener Listener to register
     */
    public void registerPaymentStatusListener(IPaymentStatusListener listener) {
        this.paymentStatusListener = listener;
    }

    @Autowired
    public VippsRestController(IPaymentStatusListener paymentStatusListener) {
        registerPaymentStatusListener(paymentStatusListener);
    }

    @PostMapping("/payments/{order_id}")
    public void callback(@PathVariable String order_id, @RequestBody PaymentCallbackInfoDTO paymentRequest,
                         @RequestHeader Map<String, String> headers) {

        logger.info("Received payment information on order: " + order_id);

        // Check if we have a registered listener and forward the message.
        if (paymentStatusListener == null) {
            logger.severe("Cannot process payment update! No payment listener registered.");
            return;
        }

        new Thread(() -> {
                // invoke the callback method of class A
                paymentStatusListener.setPaymentStatus(order_id, paymentRequest, headers.get("Authorization"));
        }).start();
    }

    /**
     * This end point implements consent withdrawal for Vipps express checkouts.
     * It is only used for express checkouts and should not be implemented otherwise.
     * @param user_id Path parameter, identifying the user which consent is being withdrawn.
     * @return 200
     * TODO! Implement a consent listener integration
     */
    @DeleteMapping("/consents/{user_id}")
    public ResponseEntity<String> removeConsents(@PathVariable String user_id) {
        return ResponseEntity.ok().build();
    }
}
