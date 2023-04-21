package academy.devdojo.springbootessentials.mapper;

import academy.devdojo.springbootessentials.domain.Cartoon;
import academy.devdojo.springbootessentials.requests.CartoonPostRequestBody;
import academy.devdojo.springbootessentials.requests.CartoonPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class CartoonMapper {
    public static final CartoonMapper INSTANCE = Mappers.getMapper(CartoonMapper.class);

    public abstract Cartoon toCartoon(CartoonPostRequestBody cartoonPostRequestBody);

    public abstract Cartoon toCartoon(CartoonPutRequestBody cartoonPutRequestBody);
}

