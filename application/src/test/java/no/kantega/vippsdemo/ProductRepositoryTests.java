package no.kantega.vippsdemo;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import no.kantega.vippsdemo.repository.IProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ContextConfiguration(classes = Application.class)
@DataMongoTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class ProductRepositoryTests {

    @Autowired
    IProductRepository repository;

    @DisplayName("Given several objects; When products are saved, they can be found.")
    @Test
    public void findAll() {
        // when
        repository.insert(new Product("Some Product", "Some Product Description", 100.0f));
        repository.insert(new Product("Some Other Product", "Some Other Product Description", 75.0f));

        // then
        assertThat(repository.findAll()).extracting("name")
            .contains("Some Product", "Some Other Product");

        // clean up
        repository.deleteAll();
    }

    @DisplayName("Given a single object Product; When it is saved, it can be found.")
    @Test
    public void findByName() {
        // when
        repository.insert(new Product("Some Product", "Some Product Description", 100.0f));
        repository.insert(new Product("Some Other Product", "Some Other Product Description", 75.0f));

        // then
        assertNotNull(repository.findByName("Some Product"));

        // then
        assertNotNull(repository.findByName("Some Other Product"));

        // then
        assertNull(repository.findByName("Some Third Product"));

        // clean up
        repository.deleteAll();
    }
}