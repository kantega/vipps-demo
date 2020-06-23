package no.kantega.vippsdemo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


class ProductTests {

    private final Product product = new Product("Car", "This is a car", 100.0f);

	@Test
    void getName() {
	    Assertions.assertEquals("Car", this.product.getName());
	}

	@Test
    void getDescription() {
        Assertions.assertEquals("This is a car", this.product.getDescription());
    }

    @Test
    void getPrice(){
        Assertions.assertEquals(100, this.product.getPrice());
    }
}
