
package com.jee.async.common.future;

import java.util.Optional;
/**
 * 三元组
 * @author yaomengke
 * @param <A>
 * @param <B>
 * @param <C>
 */
public class Tuple3<A, B, C> {

    private final A a;
    private final B b;
    private final C c;

    public Tuple3(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A a() {
        return a;
    }

    public B b() {
        return b;
    }

    public C c() {
        return c;
    }

    @Override
    public String toString() {
        return "Tuple3<" + a + "," + b + "," + c + ">";
    }

    public static <A, B, C> Tuple3<A, B, C> of(A a, B b, C c) {
        return new Tuple3<>(a, b, c);
    }

    public static <A, B, C> Tuple3<Optional<A>, Optional<B>, Optional<C>> empty() {
        return new Tuple3<>(Optional.<A>empty(), Optional.<B>empty(), Optional.<C>empty());
    }
}
