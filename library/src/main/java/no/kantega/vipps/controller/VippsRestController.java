package no.kantega.vipps.controller;

import no.kantega.vipps.IConsentStatusListener;
import no.kantega.vipps.IPaymentStatusListener;
import no.kantega.vipps.dto.PaymentCallbackInfoDTO;
import no.kantega.vipps.dto.ShippingRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    private IConsentStatusListener consentStatusListener;

    Logger logger = Logger.getLogger(VippsRestController.class.getName());

    /**
     * Call this method to register a listener for payment status updates
     * @param listener Listener to register
     */
    public void registerPaymentStatusListener(IPaymentStatusListener listener) {
        this.paymentStatusListener = listener;
    }

    /**
     * Call this method to register a listener for consent status updates
     * @param listener Listener to register
     */
    public void registerConsentStatusListener(IConsentStatusListener listener) {
        this.consentStatusListener = listener;
    }

    @Autowired
    public VippsRestController(IPaymentStatusListener paymentStatusListener, IConsentStatusListener consentStatusListener) {
        registerPaymentStatusListener(paymentStatusListener);
        registerConsentStatusListener(consentStatusListener);
    }

    /**
     * Provides a callback endpoint for Vipps
     * @param order_id Identifies the order triggering the callback.
     * @param paymentRequest Holds information on the payment request that triggered the callback.
     * @param headers Holds header information - such as auth token
     * @throws ResponseStatusException
     */
    @PostMapping("/payments/{order_id}")
    public void callback(@PathVariable String order_id, @RequestBody PaymentCallbackInfoDTO paymentRequest,
                         @RequestHeader Map<String, String> headers) {

        logger.info("Received payment information on order: " + order_id);

        // Check if we have a registered listener and forward the message.
        if (paymentStatusListener == null) {
            logger.severe("Cannot process payment update! No payment listener registered.");
        }

        Thread.UncaughtExceptionHandler exceptionHandler = (t, exc)
                -> logger.severe("Caught exception: " + exc.getMessage());

        Thread t = new Thread(() -> {
            // Invoke the callback method of the payment status listener
            try {
                paymentStatusListener.setPaymentStatus(order_id, paymentRequest, headers.get("Authorization"));
            }
            catch (IllegalArgumentException e) {
                throw e;
            }
        });

        t.setUncaughtExceptionHandler(exceptionHandler);
        t.start();
    }

    /**
     * Provides an endpoint to extract shipping details for an order.
     * @param order_id Identifies the order to retrieve shipping details on.
     * @param shippingRequest
     */
    @PostMapping("/payments/{order_id}/shippingDetails")
    public ResponseEntity<String> shippingDetails(@PathVariable String order_id, @RequestBody ShippingRequestDTO shippingRequest) {
        // Call your order service to check for order shipping details here
        // ...

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * This end point implements consent withdrawal for Vipps express checkouts.
     * It is only used for express checkouts and should not be implemented otherwise.
     * @param user_id Path parameter, identifying the user which consent is being withdrawn.
     * @return 200
     */
    @DeleteMapping("/consents/{user_id}")
    public ResponseEntity<String> removeConsents(@PathVariable String user_id) {
        logger.info("Received consent status update on user: " + user_id);

        // Check if we have a registered listener and forward the message.
        if (consentStatusListener == null) {
            logger.severe("Cannot process consent update! No consent listener registered.");
        }

        Thread.UncaughtExceptionHandler exceptionHandler = (t, exc)
                -> logger.severe("Caught exception: " + exc.getMessage());

        Thread t = new Thread(() -> {
            // Invoke the callback method of the payment status listener
            try {
                // Remove the user's consent
                consentStatusListener.setConsentStatus(user_id, null, false);
            }
            catch (IllegalArgumentException e) {
                throw e;
            }
        });

        t.setUncaughtExceptionHandler(exceptionHandler);
        t.start();

        // Vipps doesn't care about a response, so this is more a formality
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }
}
