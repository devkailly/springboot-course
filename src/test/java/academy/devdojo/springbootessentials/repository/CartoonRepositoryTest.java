package academy.devdojo.springbootessentials.repository;

import academy.devdojo.springbootessentials.domain.Cartoon;
import academy.devdojo.springbootessentials.util.CartoonCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Cartoon Repository")
@Log4j2
class CartoonRepositoryTest {
    @Autowired
    private CartoonRepository cartoonRepository;

    @Test
    @DisplayName("Save persists cartoon when Successful")
    void save_PersistCartoon_WhenSuccessful(){
        Cartoon cartoonToBeSaved = CartoonCreator.createCartoonToBeSaved();

        Cartoon cartoonSaved = this.cartoonRepository.save(cartoonToBeSaved);

        Assertions.assertThat(cartoonSaved).isNotNull();

        Assertions.assertThat(cartoonSaved.getId()).isNotNull();

        Assertions.assertThat(cartoonSaved.getName()).isEqualTo(cartoonToBeSaved.getName());
    }

    @Test
    @DisplayName("Save updates cartoon when Successful")
    void save_UpdatesCartoon_WhenSuccessful(){
        Cartoon cartoonToBeSaved = CartoonCreator.createCartoonToBeSaved();

        Cartoon cartoonSaved = this.cartoonRepository.save(cartoonToBeSaved);

        cartoonSaved.setName("Overlord");

        Cartoon cartoonUpdated = this.cartoonRepository.save(cartoonSaved);

        Assertions.assertThat(cartoonUpdated).isNotNull();

        Assertions.assertThat(cartoonUpdated.getId()).isNotNull();

        Assertions.assertThat(cartoonUpdated.getName()).isEqualTo(cartoonSaved.getName());
    }

    @Test
    @DisplayName("Delete removes cartoon when Successful")
    void delete_RemovesCartoon_WhenSuccessful(){
        Cartoon cartoonToBeSaved = CartoonCreator.createCartoonToBeSaved();

        Cartoon cartoonSaved = this.cartoonRepository.save(cartoonToBeSaved);

        this.cartoonRepository.delete(cartoonSaved);

        Optional<Cartoon> cartoonOptional = this.cartoonRepository.findById(cartoonSaved.getId());

        Assertions.assertThat(cartoonOptional).isEmpty();

    }

    @Test
    @DisplayName("Find By Name returns list of cartoon when Successful")
    void findByName_ReturnsListOfCartoon_WhenSuccessful(){
        Cartoon cartoonToBeSaved = CartoonCreator.createCartoonToBeSaved();

        Cartoon cartoonSaved = this.cartoonRepository.save(cartoonToBeSaved);

        String name = cartoonSaved.getName();

        List<Cartoon> cartoons = this.cartoonRepository.findByName(name);

        Assertions.assertThat(cartoons)
                .isNotEmpty()
                .contains(cartoonSaved);

    }

    @Test
    @DisplayName("Find By Name returns empty list when no cartoon is found")
    void findByName_ReturnsEmptyList_WhenCartoonIsNotFound(){
        List<Cartoon> cartoons = this.cartoonRepository.findByName("xaxa");

        Assertions.assertThat(cartoons).isEmpty();
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty(){
        Cartoon cartoon = new Cartoon();
//        Assertions.assertThatThrownBy(() -> this.cartoonRepository.save(cartoon))
//                .isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(jakarta.validation.ConstraintViolationException.class)
                .isThrownBy(() -> this.cartoonRepository.save(cartoon))
                .withMessageContaining("The cartoon name cannot be empty");
    }



}