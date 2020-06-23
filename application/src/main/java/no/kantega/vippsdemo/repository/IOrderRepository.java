package no.kantega.vippsdemo.repository;

import java.util.List;

import no.kantega.vippsdemo.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * IOrderRepository extends a MongoRepository,
 * and allows for repository abstraction and decoupling.
 */
public interface IOrderRepository  extends MongoRepository<Order, String> {
    List<Order> findByUserId(String id);
}
