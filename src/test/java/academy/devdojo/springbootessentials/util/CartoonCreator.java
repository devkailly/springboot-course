package academy.devdojo.springbootessentials.util;

import academy.devdojo.springbootessentials.domain.Cartoon;

public class CartoonCreator {

    public static Cartoon createCartoonToBeSaved() {
        return Cartoon.builder()
                .name("Narnia")
                .build();
    }


    public static Cartoon createValidCartoon() {
        return Cartoon.builder()
                .name("Narnia")
                .id(1L)
                .build();
    }

    public static Cartoon createValidUpdatedCartoon() {
        return Cartoon.builder()
                .name("Narnia")
                .id(1L)
                .build();
    }
}
