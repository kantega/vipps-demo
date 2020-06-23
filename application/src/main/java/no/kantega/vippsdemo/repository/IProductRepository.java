package no.kantega.vippsdemo.repository;

import no.kantega.vippsdemo.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * IProductRepository extends a MongoRepository,
 * and allows for repository abstraction and decoupling.
 */
public interface IProductRepository  extends MongoRepository<Product, String> {
    Product findByName(String name);
}
