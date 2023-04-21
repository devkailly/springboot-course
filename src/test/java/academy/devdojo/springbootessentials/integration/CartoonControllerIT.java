package academy.devdojo.springbootessentials.integration;

import academy.devdojo.springbootessentials.domain.Cartoon;
import academy.devdojo.springbootessentials.domain.DevDojoUser;
import academy.devdojo.springbootessentials.repository.CartoonRepository;
import academy.devdojo.springbootessentials.repository.DevDojoUserRepository;
import academy.devdojo.springbootessentials.requests.CartoonPostRequestBody;
import academy.devdojo.springbootessentials.util.CartoonCreator;
import academy.devdojo.springbootessentials.util.CartoonPostRequestBodyCreator;
import academy.devdojo.springbootessentials.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CartoonControllerIT {
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;
    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;
    @Autowired
    private CartoonRepository cartoonRepository;
    @Autowired
    private DevDojoUserRepository devDojoUserRepository;
    private static final DevDojoUser USER = DevDojoUser.builder()
            .name("DevDojo Academy")
            .password("{bcrypt}$2a$10$110Ht.DS50wqa8bpx7tziuO/s2dtncQzKTpDeAKQFsmnHq0/qBVuC")
            .username("devdojo")
            .authorities("ROLE_USER")
            .build();

    private static final DevDojoUser ADMIN = DevDojoUser.builder()
            .name("William Suane")
            .password("{bcrypt}$2a$10$110Ht.DS50wqa8bpx7tziuO/s2dtncQzKTpDeAKQFsmnHq0/qBVuC")
            .username("william")
            .authorities("ROLE_USER, ADMIN")
            .build();

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("devdojo", "academy");
            return new TestRestTemplate(restTemplateBuilder);
        }
        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("william", "academy");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @Test
    @DisplayName("list returns list of cartoon inside page object when successful")
    void list_ReturnsListOfCartoonsInsidePageObject_WhenSuccessful() {
        Cartoon savedCartoon = cartoonRepository.save(CartoonCreator.createCartoonToBeSaved());
        devDojoUserRepository.save(USER);

        String expectedName = savedCartoon.getName();

        PageableResponse<Cartoon> cartoonPage = testRestTemplateRoleUser.exchange("/cartoons", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Cartoon>>() {
                }).getBody();

        Assertions.assertThat(cartoonPage).isNotNull();

        Assertions.assertThat(cartoonPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(cartoonPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAll returns list of cartoon when successful")
    void listAll_ReturnsListOfCartoons_WhenSuccessful() {
        Cartoon savedCartoon = cartoonRepository.save(CartoonCreator.createCartoonToBeSaved());
        devDojoUserRepository.save(USER);

        String expectedName = savedCartoon.getName();

        List<Cartoon> cartoons = testRestTemplateRoleUser.exchange("/cartoons/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Cartoon>>() {
                }).getBody();

        Assertions.assertThat(cartoons)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(cartoons.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns cartoon when successful")
    void findById_ReturnsCartoon_WhenSuccessful() {
        Cartoon savedCartoon = cartoonRepository.save(CartoonCreator.createCartoonToBeSaved());
        devDojoUserRepository.save(USER);

        Long expectedId = savedCartoon.getId();

        Cartoon cartoon = testRestTemplateRoleUser.getForObject("/cartoons/{id}", Cartoon.class, expectedId);

        Assertions.assertThat(cartoon).isNotNull();

        Assertions.assertThat(cartoon.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a list of cartoon when successful")
    void findByName_ReturnsListOfCartoon_WhenSuccessful() {
        Cartoon savedCartoon = cartoonRepository.save(CartoonCreator.createCartoonToBeSaved());
        devDojoUserRepository.save(USER);

        String expectedName = savedCartoon.getName();

        String url = String.format("/cartoons/find?name=%s", expectedName);

        List<Cartoon> cartoons = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Cartoon>>() {
                }).getBody();

        Assertions.assertThat(cartoons)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(cartoons.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of cartoon when cartoon is not found")
    void findByName_ReturnsEmptyListOfCartoon_WhenCartoonIsNotFound() {
        devDojoUserRepository.save(USER);

        List<Cartoon> cartoons = testRestTemplateRoleUser.exchange("/cartoons/find?name=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Cartoon>>() {
                }).getBody();

        Assertions.assertThat(cartoons)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save returns cartoon when successful")
    void save_ReturnsCartoon_WhenSuccessful() {
        devDojoUserRepository.save(USER);

        CartoonPostRequestBody cartoonPostRequestBody = CartoonPostRequestBodyCreator.createCartoonPostRequestBody();

        ResponseEntity<Cartoon> cartoonResponseEntity = testRestTemplateRoleUser.postForEntity("/cartoons", cartoonPostRequestBody, Cartoon.class);

        Assertions.assertThat(cartoonResponseEntity).isNotNull();
        Assertions.assertThat(cartoonResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(cartoonResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(cartoonResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("replace updates cartoon when successful")
    void replace_UpdatesCartoon_WhenSuccessful() {
        Cartoon savedCartoon = cartoonRepository.save(CartoonCreator.createCartoonToBeSaved());
        devDojoUserRepository.save(USER);

        savedCartoon.setName("new name");

        ResponseEntity<Void> cartoonResponseEntity = testRestTemplateRoleUser.exchange("/cartoons",
                HttpMethod.PUT, new HttpEntity<>(savedCartoon), Void.class);

        Assertions.assertThat(cartoonResponseEntity).isNotNull();

        Assertions.assertThat(cartoonResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes cartoon when successful")
    void delete_RemovesCartoon_WhenSuccessful() {
        Cartoon savedCartoon = cartoonRepository.save(CartoonCreator.createCartoonToBeSaved());
        devDojoUserRepository.save(ADMIN);

        ResponseEntity<Void> cartoonResponseEntity = testRestTemplateRoleAdmin.exchange("/cartoons/admin/{id}",
                HttpMethod.DELETE, null, Void.class, savedCartoon.getId());

        Assertions.assertThat(cartoonResponseEntity).isNotNull();

        Assertions.assertThat(cartoonResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    @DisplayName("delete returns 403 when user is not admin")
    void delete_Returns403_WhenUserIsNotAdmin() {
        Cartoon savedCartoon = cartoonRepository.save(CartoonCreator.createCartoonToBeSaved());
        devDojoUserRepository.save(USER);

        ResponseEntity<Void> cartoonResponseEntity = testRestTemplateRoleUser.exchange("/cartoons/admin/{id}",
                HttpMethod.DELETE, null, Void.class, savedCartoon.getId());

        Assertions.assertThat(cartoonResponseEntity).isNotNull();

        Assertions.assertThat(cartoonResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}