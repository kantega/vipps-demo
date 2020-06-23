package no.kantega.vippsdemo.controller;

import no.kantega.vippsdemo.service.IProductService;
import no.kantega.vippsdemo.Order;
import no.kantega.vippsdemo.service.OrderService;
import no.kantega.vippsdemo.Product;
import no.kantega.vippsdemo.dto.PaymentRequestDTO;
import no.kantega.vippsdemo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

    // @PostMapping(value = "/order", consumes = "application/json", produces = "application/json")
    @PostMapping("/order")
    public String createOrder(@RequestBody Order order) {
        System.out.println("Creating order for user: " + order.getUserId() + "...");

        return orderService.createOrder(order);
    }

    @PostMapping("/initiatePayment")
    public String initiatePayment(@RequestBody PaymentRequestDTO paymentRequest){
        logger.info("Initiating payment request...");
        return paymentService.initCreatePayment(paymentRequest);
    }

    @GetMapping("/complete")
    public String completeOrder() {
        return ("<center>\n" +
                "<h1>Thank you for your purchase!</h1>\n" +
                "\n" +
                "<a href=\"/\">Return</a>\n" +
                "</center>");
    }
}
