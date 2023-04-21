package academy.devdojo.springbootessentials.service;

import academy.devdojo.springbootessentials.domain.Cartoon;
import academy.devdojo.springbootessentials.exception.BadRequestException;
import academy.devdojo.springbootessentials.mapper.CartoonMapper;
import academy.devdojo.springbootessentials.repository.CartoonRepository;
import academy.devdojo.springbootessentials.requests.CartoonPostRequestBody;
import academy.devdojo.springbootessentials.requests.CartoonPutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartoonService {

    private final CartoonRepository cartoonRepository;

    public Page<Cartoon> listAll(Pageable pegeable) {
        return cartoonRepository.findAll(pegeable);
    }

    public List<Cartoon> listAllNonPageable() {
        return cartoonRepository.findAll();
    }

    public List<Cartoon> findByName(String name) {
        return cartoonRepository.findByName(name);
    }

    public Cartoon findByIdOrThrowBadRequestException(long id) {
        return cartoonRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Cartoon not Found"));
    }

    @Transactional
    public Cartoon save(CartoonPostRequestBody cartoonPostRequestBody) {
        return cartoonRepository.save(CartoonMapper.INSTANCE.toCartoon(cartoonPostRequestBody));
    }

    public void delete(long id) {
        cartoonRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(CartoonPutRequestBody cartoonPutRequestBody) {
        Cartoon savedCartoon = findByIdOrThrowBadRequestException(cartoonPutRequestBody.getId());
        Cartoon cartoon = CartoonMapper.INSTANCE.toCartoon(cartoonPutRequestBody);
        cartoon.setId(savedCartoon.getId());
        cartoonRepository.save(cartoon);
    }
}
