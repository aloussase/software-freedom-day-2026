package io.github.aloussase.softwarefreedomday2026;

public class Tagged {

    public sealed interface Symantics<T> {
    }

    public record Bool(boolean value) implements Symantics<Boolean> {
    }

    public record Int(int value) implements Symantics<Integer> {
    }

    public record Or(Symantics<Boolean> left, Symantics<Boolean> right) implements Symantics<Boolean> {
    }

    public record Sum(Symantics<Integer> left, Symantics<Integer> right) implements Symantics<Integer> {
    }

    public static <T> T eval(Symantics<T> expr) {
        return switch (expr) {
            case Bool(final var val) -> val;
            case Int(final var val) -> val;
            case Or(final var left, final var right) -> eval(left) || eval(right);
            case Sum(final var left, final var right) -> eval(left) + eval(right);
        };
    }

}
