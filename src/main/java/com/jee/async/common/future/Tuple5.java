package com.jee.async.common.future;

import java.util.Optional;
/**
 * 五元组
 * @author yaomengke
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 * @param <E>
 */
public class Tuple5<A, B , C , D , E> {
	    private final A a;
	    private final B b;
	    private final C c;
	    private final D d;
	    private final E e;

	    public Tuple5(A a, B b, C c, D d , E e) {
	        this.a = a;
	        this.b = b;
	        this.c = c;
	        this.d = d;
	        this.e = e;
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
	    
	    public E e(){
	    	return e;
	    }
	    
	    

	    @Override
	    public String toString() {
	        return "Tuple5<" + a + "," + b + "," + c + "," + d + "," + e  +">";
	    }

	    public static <A, B, C, D, E> Tuple5<A, B, C, D , E> of(A a, B b, C c, D d, E e) {
	        return new Tuple5<>(a, b, c, d , e);
	    }

	    public static <A, B, C, D , E> Tuple5<Optional<A>, Optional<B>, Optional<C>, Optional<D> , Optional<E>> empty() {
	        return new Tuple5<>(Optional.<A>empty(), Optional.<B>empty(), Optional.<C>empty(), Optional.<D>empty() ,Optional.<E>empty());
	    }

}
