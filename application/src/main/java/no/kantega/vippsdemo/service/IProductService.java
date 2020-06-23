package no.kantega.vippsdemo.service;

import no.kantega.vippsdemo.Product;

public interface IProductService {
    public Iterable<Product> getAllProducts();
    public Product getProductByName(String name);
    public String createProduct(Product product);
}