package no.kantega.vippsdemo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class VippsLoginIntegrationTest {

    final private static String baseUrl = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test() {
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(baseUrl + port + "/access",
                HttpMethod.POST, null, String.class
        );

        String vehiclesResponseList = responseEntity.getBody();
    }


}
