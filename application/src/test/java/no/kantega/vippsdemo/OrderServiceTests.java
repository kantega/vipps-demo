package no.kantega.vippsdemo;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import no.kantega.vippsdemo.Order;
import no.kantega.vippsdemo.service.OrderService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.apache.commons.collections4.IterableUtils;

import no.kantega.vippsdemo.repository.IOrderRepository;


public class OrderServiceTests {

    IOrderRepository repository = mock(IOrderRepository.class);
    private final OrderService orderService = new OrderService(repository);

    @Test
    public void getAllOrders() {
        Assertions.assertEquals(0, IterableUtils.size(orderService.getAllOrders()));

        Order order = mock(Order.class);
        List<Order> orders = new ArrayList<Order>();
        orders.add(order);

        when(repository.findAll()).thenReturn(orders);
        Assertions.assertEquals(1, IterableUtils.size(orderService.getAllOrders()));
    }

    @Test
    public void getOrderById() {
        Assertions.assertEquals(Optional.empty(), orderService.getOrderById("some id"));

        Order order = mock(Order.class);
        when(repository.findById(anyString())).thenReturn(Optional.of(order));
        Assertions.assertEquals(Optional.of(order), orderService.getOrderById("some id"));
    }

    @Test
    public void getOrderByUserId() {
        Assertions.assertEquals(0, IterableUtils.size(orderService.getOrderByUserId("some id")));

        // Create a list of orders to return from the mock repository
        List<Order> orders = new ArrayList<Order>();
        Order order = mock(Order.class);
        Order order2 = mock(Order.class);
        orders.add(order);
        orders.add(order2);

        when(order.getUserId()).thenReturn("some id");
        when(order2.getUserId()).thenReturn("some id");
        when(repository.findByUserId(anyString())).thenReturn(orders);

        // Expect the query to return two out of the three orders
        Assertions.assertEquals(2, IterableUtils.size(orderService.getOrderByUserId("some id")));
    }

    @Test
    public void createOrder() {
        Order order = mock(Order.class);
        ObjectId objectId = new ObjectId();

        when(repository.insert(order)).thenReturn(order);
        when(order.getId()).thenReturn(objectId);

        String order_id = orderService.createOrder(order);
        Assertions.assertEquals(objectId.toString(), order_id);
    }

    @Test
    public void setOrderStatus() {
        // Mock an order to update
        Order order = mock(Order.class);

        // Return this order when requested and when saved
        when(repository.save(order)).thenReturn(order);
        when(repository.findById(anyString())).thenReturn(Optional.of(order));

        // Check that the request to update order is successful
        assertTrue(orderService.setOrderStatus("some object id", "some status"));
    }

    @Test
    public void setOrderStatusOrderNotFound() {
        // When the order is requested, return an empty object
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        // Check that the request to update order fails
        assertFalse(orderService.setOrderStatus("some object id", "some status"));
    }
}
