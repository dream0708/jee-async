
package com.jee.async.common.future;

import java.util.Optional;
/**
 * 四元组
 * @author yaomengke
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 */
public class Tuple4<A, B, C, D> {

    private final A a;
    private final B b;
    private final C c;
    private final D d;

    public Tuple4(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
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

    public D d() {
        return d;
    }

    @Override
    public String toString() {
        return "Tuple4<" + a + "," + b + "," + c + "," + d + ">";
    }

    public static <A, B, C, D> Tuple4<A, B, C, D> of(A a, B b, C c, D d) {
        return new Tuple4<>(a, b, c, d);
    }

    public static <A, B, C, D> Tuple4<Optional<A>, Optional<B>, Optional<C>, Optional<D>> empty() {
        return new Tuple4<>(Optional.<A>empty(), Optional.<B>empty(), Optional.<C>empty(), Optional.<D>empty());
    }
}
