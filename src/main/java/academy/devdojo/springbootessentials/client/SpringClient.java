package academy.devdojo.springbootessentials.client;

import academy.devdojo.springbootessentials.domain.Cartoon;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Cartoon> entity = new RestTemplate().getForEntity("http://localhost:8080/cartoons/{id}",
                Cartoon.class,24);
        log.info(entity);

        Cartoon object = new RestTemplate().getForObject("http://localhost:8080/cartoons/{id}", Cartoon.class,24);

        log.info(object);

        Cartoon[] cartoons = new RestTemplate().getForObject("http://localhost:8080/cartoons/all", Cartoon[].class);

        log.info(Arrays.toString(cartoons));

        ResponseEntity<List<Cartoon>> exchange = new RestTemplate().exchange("http://localhost:8080/cartoons/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Cartoon>>() {
                });

        log.info(exchange.getBody());

        //Cartoon kingdom = Cartoon.builder().name("kingdom").build();
        // Cartoon kingdomSaved = new RestTemplate().postForObject("http://localhost:8080/cartoons/", kingdom, Cartoon.class);
        // log.info("saved cartoon {}", kingdomSaved);

        Cartoon samuraiJack = Cartoon.builder().name("Samurai Jack").build();
        ResponseEntity<Cartoon> samuraiJackSaved = new RestTemplate().exchange("http://localhost:8080/cartoons",
                HttpMethod.POST,
                new HttpEntity<>(samuraiJack, createJsonHeader()),
        Cartoon.class);

        log.info("saved cartoon {}", samuraiJackSaved);


        Cartoon cartoonToBeUpdated = samuraiJackSaved.getBody();
        cartoonToBeUpdated.setName("Samurai Jack 2");

        ResponseEntity<Void> samuraiJackUpdated = new RestTemplate().exchange("http://localhost:8080/cartoons",
                HttpMethod.PUT,
                new HttpEntity<>(cartoonToBeUpdated, createJsonHeader()),
                Void.class);

        log.info(samuraiJackUpdated);


        Cartoon cartoonToBeDelete = samuraiJackSaved.getBody();
        cartoonToBeDelete.setName("Samurai Jack 2");

        ResponseEntity<Void> samuraiJackDeleted = new RestTemplate().exchange("http://localhost:8080/cartoons/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                cartoonToBeUpdated.getId());

        log.info(samuraiJackDeleted);
    }

    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;

    }
}
