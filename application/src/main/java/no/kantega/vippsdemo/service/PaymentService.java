package no.kantega.vippsdemo.service;

import no.kantega.vippsdemo.dto.PaymentRequestDTO;
import no.kantega.vippsdemo.dto.ProductDTO;
import no.kantega.vipps.service.VippsService;
import no.kantega.vippsdemo.Order;
import no.kantega.vippsdemo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PaymentService {

    private VippsService vippsService;
    private OrderService orderService;
    private ProductService productService;

    @Value("${transaction.text}")
    private String TRANSACTION_TEXT;

    Logger logger = Logger.getLogger(PaymentService.class.getName());

    @Autowired
    public PaymentService(VippsService vippsService, OrderService orderService, ProductService productService) {
        this.vippsService = vippsService;
        this.orderService = orderService;
        this.productService = productService;
    }

    /**
     * Creates an order and a payment transaction based on a given payment request.
     * The payment request is populated with missing information as the transaction is being processed.
     * @param paymentRequest Contains payment information to be used for creating the order/payment transaction.
     * @return An order id for the payment request.
     * @throws IllegalArgumentException if input arguments fail.
     */
    @Transactional
    public String initCreatePayment(PaymentRequestDTO paymentRequest) throws IllegalArgumentException {
        logger.info("Creating payment...");

        // Iterate the products requested to purchase and calculate the price to pay
        double totalAmount = 0;
        for ( ProductDTO requestProduct : paymentRequest.getProducts() ) {

            logger.info("Fetching product details for '" + requestProduct.getName() + "'...");
            Product product = productService.getProductByName(requestProduct.getName());
            if (product == null) {
                logger.log(Level.SEVERE, "Could not find the product!");
                throw new IllegalArgumentException("product is no longer available");
            }

            totalAmount += product.getPrice() * requestProduct.getQuantity();
        }

        // TODO! Put this logic in the service module
        if (totalAmount < 100) {
            logger.log(Level.WARNING, "The total is below minimum value! The order will not be processed!");
            throw new IllegalArgumentException("product total is below minimum charge");
        }

        // Create a new order and retrieve its order id
        Order order = new Order(paymentRequest.getUserId());
        String orderId = orderService.createOrder(order);
        logger.info("Order '" + orderId + "' created");

        // TODO! Consider using PaymentRequestDTO.transactionDescription as transaction text
        // Request a Vipps payment, providing an order id,
        // the total amount to pay and a transaction description.
        // If no mobile number is provided, the user will be prompted for one,
        // when she reaches the Vipps landing page returned as 'url'
        return vippsService.createPayment(
                paymentRequest.getMobileNumber(),
                orderId,
                totalAmount,
                TRANSACTION_TEXT);
    }

    /**
     * Call this to update a payment order status.
     * @param order_id Holds the id of the order to update.
     * @param status Holds the new status for the order.
     */
    public void updateOrderStatus(String order_id, String status, String authToken) {
        logger.info("Processing order status for: " + order_id);
        logger.info("Auth token is: " + authToken);

        // Fetch the order from our repository
        Optional<Order> order = orderService.getOrderById(order_id);

        // Check if we have an object (which may not be the case)
        if (!order.isPresent()) {
            logger.severe(String.format("Could not find order id: %s\n" +
                    "Aborting payment processing...", order_id));
            return;
        }

        // Check for matching auth tokens
        String orderAuthToken = order.get().getAuthToken();

        // A null value, or empty string indicates no need for a token match
        boolean authTokenMatch = orderAuthToken == null || orderAuthToken.isEmpty();

        if (!authTokenMatch && authToken != null) // The token is not null and we need to check for a hard match
            authTokenMatch = orderAuthToken.equals(authToken);

        if (!authTokenMatch) {
            logger.severe("Authorization tokens do not match! Cannot update order status.");
            return;
        }

        // Set the new order status
        logger.info("Setting order status to: " + status);
        orderService.setOrderStatus(order_id, status);
    }
}
