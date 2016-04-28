
package com.jee.async.common.future;

import java.util.Optional;
/**
 * 两元组
 * @author yaomengke
 * @param <A>
 * @param <B>
 */
public class Tuple2<A, B> {

    private final A a;
    private final B b;

    public Tuple2(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A a() {
        return a;
    }

    public B b() {
        return b;
    }

    @Override
    public String toString() {
        return "Tuple2<" + a + "," + b + ">";
    }

    public static <A, B> Tuple2<A, B> of(A a, B b) {
        return new Tuple2<>(a, b);
    }

    public static <A, B> Tuple2<Optional<A>, Optional<B>> empty() {
        return new Tuple2<>(Optional.<A>empty(), Optional.<B>empty());
    }
}
