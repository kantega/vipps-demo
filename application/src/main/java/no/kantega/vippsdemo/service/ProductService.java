package no.kantega.vippsdemo.service;

import java.lang.Iterable;

import no.kantega.vippsdemo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.kantega.vippsdemo.repository.IProductRepository;

/**
 * The ProductService provides an interface for creating
 * and retrieving products.
 */
@Service
public class ProductService implements IProductService {

    private IProductRepository repository;

    @Autowired
    public ProductService(IProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public Product getProductByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public String createProduct(Product product) {
        return repository.insert(product).toString();
    }
}