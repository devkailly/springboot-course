package academy.devdojo.springbootessentials.util;

import academy.devdojo.springbootessentials.domain.Cartoon;
import academy.devdojo.springbootessentials.requests.CartoonPostRequestBody;

public class CartoonPostRequestBodyCreator {
    public static CartoonPostRequestBody createCartoonPostRequestBody(){
        return CartoonPostRequestBody.builder()
                .name(CartoonCreator.createCartoonToBeSaved().getName())
                .build();
    }
}
