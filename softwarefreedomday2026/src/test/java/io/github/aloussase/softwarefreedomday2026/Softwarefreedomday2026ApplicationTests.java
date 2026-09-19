package io.github.aloussase.softwarefreedomday2026;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Softwarefreedomday2026ApplicationTests {

    @Test
    void test1_tagged() {
        final var expr = new Tagged.App<>(
                new Tagged.Lam<>(Function.identity()),
                new Tagged.Bool(true)
        );

        final var e2 = new Tagged.Lam<>(x -> x);
        final var r2 = Tagged.eval(e2);

        final var result = Tagged.eval(expr);

        assertEquals(new Tagged.Bool(true), result);
    }

}
