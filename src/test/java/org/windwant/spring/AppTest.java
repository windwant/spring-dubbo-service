package org.windwant.spring;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for simple BootSpring.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AppTest 
{
    @Autowired
    private TestRestTemplate restTemplate;

    @org.junit.Test
    public void testApp()
    {
        String body = this.restTemplate.getForObject("/hello", String.class);
        System.out.println(body);
        assertThat(body).isEqualTo("{\"msg\":\"OK\",\"code\":0}");
    }
}
