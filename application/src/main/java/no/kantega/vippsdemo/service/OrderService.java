package no.kantega.vippsdemo.service;

import java.lang.Iterable;
import java.util.Optional;

import no.kantega.vippsdemo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.kantega.vippsdemo.repository.IOrderRepository;

@Service
public class OrderService {

    private IOrderRepository repository;

    @Autowired
    public OrderService(IOrderRepository repository) {
        this.repository = repository;
    }

    public Iterable<Order> getAllOrders(){
        return repository.findAll();
    }

    public Optional<Order> getOrderById(String id){
        return repository.findById(id);
    }

    public Iterable<Order> getOrderByUserId(String userId){
        return repository.findByUserId(userId);
    }

    public String createOrder(Order order) {
        return repository.insert(order).getId().toString();
    }

    /**
     * Set new status for an existing order.
     * @param id Holds the id of the order to update.
     * @param status Holds the status to update to.
     * @return true if order was updated - otherwise false.
     */
    public boolean setOrderStatus(String id, String status) {
        Optional<Order> order = repository.findById(id);

        try {
            if (order.isPresent()) {
                Order orderItem = order.get();
                orderItem.setStatus(status);
                repository.save(orderItem);
                return true;
            }
        }
        catch (IllegalArgumentException e) {
            // Do nothing
        }

        return false;
    }
}
