package academy.devdojo.springbootessentials.util;

import academy.devdojo.springbootessentials.requests.CartoonPutRequestBody;

public class CartoonPutRequestBodyCreator {
    public static CartoonPutRequestBody createCartoonPutRequestBody(){
        return CartoonPutRequestBody.builder()
                .id(CartoonCreator.createValidUpdatedCartoon().getId())
                .name(CartoonCreator.createValidUpdatedCartoon().getName())
                .build();
    }
}
