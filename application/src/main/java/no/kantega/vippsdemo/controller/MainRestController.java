package no.kantega.vippsdemo.controller;

import no.kantega.vippsdemo.service.IProductService;
import no.kantega.vippsdemo.Order;
import no.kantega.vippsdemo.service.OrderService;
import no.kantega.vippsdemo.Product;
import no.kantega.vippsdemo.dto.PaymentRequestDTO;
import no.kantega.vippsdemo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RequestMapping("/")
@RestController
public class MainRestController {

    private IProductService productService;
    private OrderService orderService;
    private PaymentService paymentService;

    Logger logger = Logger.getLogger(MainRestController.class.getName());

    @Autowired
    public MainRestController(IProductService productService,
                              OrderService orderService,
                              PaymentService paymentService) {
        this.productService = productService;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/product")
    public Iterable<Product> getProduct(@RequestParam(value = "name", required = false) String name) {
        if (name != null) {
            List<Product> products = new ArrayList<>();
            products.add(productService.getProductByName(name));
            return products;
        }

        return productService.getAllProducts();
    }

    @PostMapping("/product")
    public String createProduct(@RequestBody Product product) {
        System.out.println("Creating new product: " + product.getName() + "...");

        return productService.createProduct(product);
    }

    @GetMapping("/order")
    public Iterable<Order> getOrder(@RequestParam(value = "user_id", required = false) String user_id) {
        System.out.println("Retrieving order for user: " + user_id + "...");

        if (user_id != null)
            return orderService.getOrderByUserId(user_id);

        return orderService.getAllOrders();
    }

    /**
     * This endpoint will return an order, based on the input order id.
     * TODO! Consider uniting/reusing this for payment completion (see the completeOrder() method)
     */
    @GetMapping("/order/{order_id}")
    public Optional<Order> getOrderById(@PathVariable String order_id) {
        logger.info("Retrieving order " + order_id);
        return orderService.getOrderById(order_id);
    }

    // @PostMapping(value = "/order", consumes = "application/json", produces = "application/json")
    @PostMapping("/order")
    public String createOrder(@RequestBody Order order) {
        System.out.println("Creating order for user: " + order.getUserId() + "...");

        return orderService.createOrder(order);
    }

    /**
     * Call this to start processing payment.
     * @param paymentRequest Contains information about the payment.
     * @return An order id for the processed payment.
     */
    @PostMapping("/initiatePayment")
    public String initiatePayment(@RequestBody PaymentRequestDTO paymentRequest){
        logger.info("Initiating payment request...");
        return paymentService.initCreatePayment(paymentRequest);
    }

    /**
     * Fallback page for order completion with Vipps
     * TODO! Make this more refined!
     *
     * @param order_id The id of the order recently processed.
     * @return Html formatted string containing current order status,
     * or 404 response status exception if not found.
     */
    @GetMapping("/complete/{order_id}")
    public String completeOrder(@PathVariable String order_id) {
        logger.info("Completing order for " + order_id);
        Optional<Order> order = orderService.getOrderById(order_id);

        if (!order.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "order not found"
            );
        }

        String message = "Your order is being processed...";

        if (order.get().getStatus().equals("SALE")) {
            message = "Your payment has been registered.";
        }

        return ("<center>\n" +
                String.format("<h1>%s</h1>\n\n", message) +
                "<a href=\"/\">Return</a>\n" +
                "</center>");
    }
}
