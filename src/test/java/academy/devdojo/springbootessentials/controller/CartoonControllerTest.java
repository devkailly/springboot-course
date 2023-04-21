package academy.devdojo.springbootessentials.controller;

import academy.devdojo.springbootessentials.domain.Cartoon;
import academy.devdojo.springbootessentials.requests.CartoonPostRequestBody;
import academy.devdojo.springbootessentials.requests.CartoonPutRequestBody;
import academy.devdojo.springbootessentials.service.CartoonService;
import academy.devdojo.springbootessentials.util.CartoonCreator;
import academy.devdojo.springbootessentials.util.CartoonPostRequestBodyCreator;
import academy.devdojo.springbootessentials.util.CartoonPutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class CartoonControllerTest {
    @InjectMocks
    private CartoonController cartoonController;
    @Mock
    private CartoonService cartoonServiceMock;

    @BeforeEach
    void setUp(){
        PageImpl<Cartoon> cartoonPage = new PageImpl<>(List.of(CartoonCreator.createValidCartoon()));
        BDDMockito.when(cartoonServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(cartoonPage);

        BDDMockito.when(cartoonServiceMock.listAllNonPageable())
                .thenReturn(List.of(CartoonCreator.createValidCartoon()));

        BDDMockito.when(cartoonServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(CartoonCreator.createValidCartoon());

        BDDMockito.when(cartoonServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(CartoonCreator.createValidCartoon()));

        BDDMockito.when(cartoonServiceMock.save(ArgumentMatchers.any(CartoonPostRequestBody.class)))
                .thenReturn(CartoonCreator.createValidCartoon());

        BDDMockito.doNothing().when(cartoonServiceMock).replace(ArgumentMatchers.any(CartoonPutRequestBody.class));

        BDDMockito.doNothing().when(cartoonServiceMock).delete(ArgumentMatchers.anyLong());
    }
    @Test
    @DisplayName("list returns list of cartoon inside page object when successful")
    void list_ReturnsListOfCartoonsInsidePageObject_WhenSuccessful(){
        String expectedName = CartoonCreator.createValidCartoon().getName();

        Page<Cartoon> cartoonPage = cartoonController.list(null).getBody();

        Assertions.assertThat(cartoonPage).isNotNull();

        Assertions.assertThat(cartoonPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(cartoonPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAll returns list of cartoon when successful")
    void listAll_ReturnsListOfCartoons_WhenSuccessful(){
        String expectedName = CartoonCreator.createValidCartoon().getName();

        List<Cartoon> cartoons = cartoonController.listAll().getBody();

        Assertions.assertThat(cartoons)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(cartoons.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns cartoon when successful")
    void findById_ReturnsCartoon_WhenSuccessful(){
        Long expectedId = CartoonCreator.createValidCartoon().getId();

        Cartoon cartoon = cartoonController.findById(1).getBody();

        Assertions.assertThat(cartoon).isNotNull();

        Assertions.assertThat(cartoon.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a list of cartoon when successful")
    void findByName_ReturnsListOfCartoon_WhenSuccessful(){
        String expectedName = CartoonCreator.createValidCartoon().getName();

        List<Cartoon> cartoons = cartoonController.findByName("cartoon").getBody();

        Assertions.assertThat(cartoons)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(cartoons.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of cartoon when cartoon is not found")
    void findByName_ReturnsEmptyListOfCartoon_WhenCartoonIsNotFound(){
        BDDMockito.when(cartoonServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Cartoon> cartoons = cartoonController.findByName("cartoon").getBody();

        Assertions.assertThat(cartoons)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save returns cartoon when successful")
    void save_ReturnsCartoon_WhenSuccessful(){

        Cartoon cartoon = cartoonController.save(CartoonPostRequestBodyCreator.createCartoonPostRequestBody()).getBody();

        Assertions.assertThat(cartoon).isNotNull().isEqualTo(CartoonCreator.createValidCartoon());

    }

    @Test
    @DisplayName("replace updates cartoon when successful")
    void replace_UpdatesCartoon_WhenSuccessful(){

        Assertions.assertThatCode(() ->cartoonController.replace(CartoonPutRequestBodyCreator.createCartoonPutRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = cartoonController.replace(CartoonPutRequestBodyCreator.createCartoonPutRequestBody());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes cartoon when successful")
    void delete_RemovesCartoon_WhenSuccessful(){

        Assertions.assertThatCode(() ->cartoonController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = cartoonController.delete(1);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}