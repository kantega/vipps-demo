package no.kantega.vippsdemo;

import static org.mockito.Mockito.*;

import no.kantega.vippsdemo.Order;
import no.kantega.vippsdemo.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class OrderTests {

    // Create some mock products to be put into our orders
    Product p1 = mock(Product.class);
    Product p2 = mock(Product.class);
    Product p3 = mock(Product.class);

    @Test
    public void addProduct(){
        Order order = new Order("someUserId");

        Assertions.assertEquals(0, order.getAllProducts().size());
        order.addProduct(p1);
        order.addProduct(p2);

        Assertions.assertEquals(2, order.getAllProducts().size());
    }

    @Test
    public void removeProduct(){
        Order order = new Order("someUserId");

        order.addProduct(p1);
        order.addProduct(p2);
        Assertions.assertEquals(2, order.getAllProducts().size());

        order.removeProduct(p2);
        Assertions.assertEquals(1, order.getAllProducts().size());
    }

    @Test
    public void getTotalOrderPrice(){
        Order order = new Order("someUserId");
        Assertions.assertEquals(0F, order.getTotalOrderPrice());

        order.addProduct(p1);
        when(p1.getPrice()).thenReturn(100F);
        Assertions.assertEquals(100F, order.getTotalOrderPrice());

        order.addProduct(p2);
        when(p2.getPrice()).thenReturn(50F);
        Assertions.assertEquals(150F, order.getTotalOrderPrice());
    }
}
