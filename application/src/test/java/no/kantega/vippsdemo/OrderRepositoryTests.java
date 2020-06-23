package no.kantega.vippsdemo;

import static org.assertj.core.api.Assertions.*;

import no.kantega.vippsdemo.repository.IOrderRepository;
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
public class OrderRepositoryTests {

    @Autowired
    IOrderRepository repository;

    @DisplayName("Given several objects; When orders are saved, they can be found.")
    @Test
    public void findAll() {
        String user_id_1 = "user_id_1";
        String user_id_2 = "user_id_2";

        // when
        repository.insert(new Order(user_id_1));
        repository.insert(new Order(user_id_2));

        // then
        assertThat(repository.findAll()).extracting("userId")
            .contains("user_id_1", "user_id_2");

        // clean up
        repository.deleteAll();
    }

    @DisplayName("Given a single object Product; When it is saved, it can be found.")
    @Test
    public void findByUserId() {
        String user_id_1 = "user_id_1";
        String user_id_2 = "user_id_2";

        // when
        repository.insert(new Order(user_id_1));
        repository.insert(new Order(user_id_2));

        // then
        assertThat(repository.findByUserId("user_id_1")).extracting("userId")
            .contains("user_id_1")
            .doesNotContain("user_id_2");

        // then
        assertThat(repository.findByUserId("user_id_3")).isEmpty();

        // clean up
        repository.deleteAll();
    }
}
