package com.jee.async.common.future;

import java.util.Optional;
/**
 * 全局数据 三元组 
 * 方便用于CompletableFuture数据提取
 * @author yaomengke
 * @param <A>
 * @param <B>
 * @param <C>
 */
public class Global<A, B , C > implements java.io.Serializable {
	 
	    private static final long serialVersionUID = 6804045903635966273L;
		private  A a;
	    private  B b;
	    private  C c;

	    public Global(A a, B b, C c) {
	        this.a = a;
	        this.b = b;
	        this.c = c;
	    }

	    public Global() {
		  
		}

		public void a(A a) {
	       this.a = a;
	    }

	    public void b(B b) {
	       this.b = b;
	    }

	    public void c(C c) {
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

        	    

	    public A getA() {
			return a;
		}

		public void setA(A a) {
			this.a = a;
		}

		public B getB() {
			return b;
		}

		public void setB(B b) {
			this.b = b;
		}

		public C getC() {
			return c;
		}

		public void setC(C c) {
			this.c = c;
		}

		

		@Override
	    public String toString() {
	        return "Global<" + a + "," + b + "," + c + ">";
	    }

	    public static <A, B, C, D, E> Global<A, B, C> of(A a, B b, C c) {
	        return new Global<>(a, b, c);
	    }

	    public static <A, B, C> Global<Optional<A>, Optional<B>, Optional<C>> empty() {
	        return new Global<>(Optional.<A>empty(), Optional.<B>empty(), Optional.<C>empty());
	    }

}
