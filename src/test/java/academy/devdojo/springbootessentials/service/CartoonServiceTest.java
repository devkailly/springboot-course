package academy.devdojo.springbootessentials.service;

import academy.devdojo.springbootessentials.domain.Cartoon;
import academy.devdojo.springbootessentials.exception.BadRequestException;
import academy.devdojo.springbootessentials.repository.CartoonRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class CartoonServiceTest {
    @InjectMocks
    private CartoonService cartoonService;
    @Mock
    private CartoonRepository cartoonRepositoryMock;

    @BeforeEach
    void setUp(){
        PageImpl<Cartoon> cartoonPage = new PageImpl<>(List.of(CartoonCreator.createValidCartoon()));
        BDDMockito.when(cartoonRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(cartoonPage);

        BDDMockito.when(cartoonRepositoryMock.findAll())
                .thenReturn(List.of(CartoonCreator.createValidCartoon()));

        BDDMockito.when(cartoonRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(CartoonCreator.createValidCartoon()));

        BDDMockito.when(cartoonRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(CartoonCreator.createValidCartoon()));

        BDDMockito.when(cartoonRepositoryMock.save(ArgumentMatchers.any(Cartoon.class)))
                .thenReturn(CartoonCreator.createValidCartoon());

        BDDMockito.doNothing().when(cartoonRepositoryMock).delete(ArgumentMatchers.any(Cartoon.class));
    }
    @Test
    @DisplayName("listAll returns list of cartoon inside page object when successful")
    void listAll_ReturnsListOfCartoonInsidePageObject_WhenSuccessful(){
        String expectedName = CartoonCreator.createValidCartoon().getName();

        Page<Cartoon> cartoonPage = cartoonService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(cartoonPage).isNotNull();

        Assertions.assertThat(cartoonPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(cartoonPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllNonPageable returns list of cartoon when successful")
    void listAllNonPageable_ReturnsListOfCartoon_WhenSuccessful(){
        String expectedName = CartoonCreator.createValidCartoon().getName();

        List<Cartoon> cartoons = cartoonService.listAllNonPageable();

        Assertions.assertThat(cartoons)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(cartoons.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns cartoon when successful")
    void findByIdOrThrowBadRequestException_ReturnsCartoon_WhenSuccessful(){
        Long expectedId = CartoonCreator.createValidCartoon().getId();

        Cartoon cartoon = cartoonService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(cartoon).isNotNull();

        Assertions.assertThat(cartoon.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when cartoon is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenCartoonIsNotFound(){
        BDDMockito.when(cartoonRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> cartoonService.findByIdOrThrowBadRequestException(1));
    }

    @Test
    @DisplayName("findByName returns a list of cartoon when successful")
    void findByName_ReturnsListOfCartoon_WhenSuccessful(){
        String expectedName = CartoonCreator.createValidCartoon().getName();

        List<Cartoon> cartoons = cartoonService.findByName("cartoon");

        Assertions.assertThat(cartoons)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(cartoons.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of cartoon when cartoon is not found")
    void findByName_ReturnsEmptyListOfCartoon_WhenCartoonIsNotFound(){
        BDDMockito.when(cartoonRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Cartoon> cartoons = cartoonService.findByName("cartoon");

        Assertions.assertThat(cartoons)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save returns cartoon when successful")
    void save_ReturnsCartoon_WhenSuccessful(){

        Cartoon cartoon = cartoonService.save(CartoonPostRequestBodyCreator.createCartoonPostRequestBody());

        Assertions.assertThat(cartoon).isNotNull().isEqualTo(CartoonCreator.createValidCartoon());

    }

    @Test
    @DisplayName("replace updates cartoon when successful")
    void replace_UpdatesCartoon_WhenSuccessful(){

        Assertions.assertThatCode(() ->cartoonService.replace(CartoonPutRequestBodyCreator.createCartoonPutRequestBody()))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("delete removes cartoon when successful")
    void delete_RemovesCartoon_WhenSuccessful(){

        Assertions.assertThatCode(() ->cartoonService.delete(1))
                .doesNotThrowAnyException();

    }
}