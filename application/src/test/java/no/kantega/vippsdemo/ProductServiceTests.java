package no.kantega.vippsdemo;

import java.util.ArrayList;

import no.kantega.vippsdemo.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.mockito.Mockito.*;

import org.apache.commons.collections4.IterableUtils;

import no.kantega.vippsdemo.repository.IProductRepository;


public class ProductServiceTests {

    Product product = mock(Product.class);

    IProductRepository repository = mock(IProductRepository.class);;
    private ProductService productService = new ProductService(repository);

    @Test
    public void getAllProducts() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertEquals(0, IterableUtils.size(productService.getAllProducts()));
    }

    @Test
    public void getProductByName() {
        when(repository.findByName(anyString())).thenReturn(product);
        when(product.getName()).thenReturn("Some name");
        Assertions.assertEquals("Some name", productService.getProductByName("name").getName());
    }

    @Test
    public void createProductSuccess() {
        when(product.toString()).thenReturn("{\n" +
                "   \"name\": \"some name\",\n" +
                "   \"description\": \"some description\",\n" +
                "   \"price\": 200.00\n" +
                "}"
        );
        when(repository.insert(product)).thenReturn(product);
        Assertions.assertTrue(StringUtils.containsIgnoreCase(productService.createProduct(product), "some name"));
    }
}