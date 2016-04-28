
package com.jee.async.common.future;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/**
 * 线程控制模块封装
 * 串行  、 并行 、所有 
 * @author yaomengke
 *
 */
public final class CompletableFutureUtils {

	private static Logger logger = LoggerFactory.getLogger(CompletableFutureUtils.class);

	private CompletableFutureUtils() {
	}
    /**
     * 异常处理 给有异常的线程返回默认值
     * @param defaultValue
     * @return
     */
	public static <R> BiFunction<R, Throwable, R> withFallback(R defaultValue) {
		return (f, t) -> {
			if (t != null) {
				logger.debug("Throws exceptions , and return a default value of : " + defaultValue);
				return defaultValue;
			} else {
				logger.debug("right steps , return value of " + f);
				return f;
			}
		};
	}
   
	public static <A> CompletableFuture<Optional<A>> none() {
		return CompletableFuture.completedFuture(Optional.<A> empty());
	}
    
	/**
	 * 返回一个CompletableFuture 
	 * 立即结束 线程的返回值为Optional(a)
	 * @param a
	 * @return
	 */
	public static <A> CompletableFuture<Optional<A>> some(A a) {
		return CompletableFuture.completedFuture(Optional.<A> ofNullable(a));
	}

	/**
	 * 返回一个CompletableFuture 
	 * 立即结束 线程的返回值为a
	 * @param a
	 * @return
	 */
	public static <A> CompletableFuture<A> one(A a) {
		return CompletableFuture.completedFuture(a);
	}
	
	/**
	 * 创建CompletableFuture
	 * @param supplier
	 * @param executor
	 * @return
	 */
	public static<A> CompletableFuture<A> one(Supplier<A> supplier , ThreadPoolExecutor executor){
		return CompletableFuture.supplyAsync(supplier ,  executor ) ;
	}
	/**
	 * 创建CompletableFuture
	 * @param supplier
	 * @param executor
	 * @return
	 */
	public static<A> CompletableFuture<A> one(Supplier<A> supplier , ThreadPoolTaskExecutor executor){
		return CompletableFuture.supplyAsync(supplier ,  executor ) ;
	}
	
	
   
	/**
	 * 两个线程串行执行 A->B
	 * 串行执行 fnAB中CompletableFuture依赖futureA
	 * @param futureA
	 * @param fnAB
	 * @return CompletableFuture<B>
	 */
	
	public static <A, B> CompletableFuture<B> sequential(CompletableFuture<A> futureA,
			Function<A, CompletableFuture<B>> fnAB) {

		return futureA.thenCompose(fnAB);
	}
	
	/**三个线程 串行执行  A->B->C
	 * functionBC中CompletableFuture 执行依赖functionAB中CompletableFuture执行完毕数据
	 * functionAB中CompletableFuture依赖futureA 执行完毕数据
	 * @param futureA
	 * @param functionAB
	 * @param functionBC
	 * @return CompletableFuture<C>
	 */
	
	public static <A, B, C> CompletableFuture<C> sequential(CompletableFuture<A> futureA,
			Function<A, CompletableFuture<B>> functionAB, Function<B, CompletableFuture<C>> functionBC) {

		return futureA.thenCompose(functionAB).thenCompose(functionBC);
	}
    
	/**四个线程串行执行 A->B->C->D 
	 * functionCD中CompletableFuture 执行依赖functionBC中CompletableFuture执行完毕数据
	 * functionBC中CompletableFuture 执行依赖functionAB中CompletableFuture执行完毕数据
	 * functionAB中CompletableFuture依赖futureA 执行完毕数据
	 * @param futureA
	 * @param functionAB
	 * @param functionBC
	 * @param functionCD
	 * @return CompletableFuture<D>
	 */
	public static <A, B, C, D> CompletableFuture<D> sequential(CompletableFuture<A> futureA,
			Function<A, CompletableFuture<B>> functionAB, Function<B, CompletableFuture<C>> functionBC,
			Function<C, CompletableFuture<D>> functionCD) {

		return futureA.thenCompose(functionAB).thenCompose(functionBC).thenCompose(functionCD);
	}
	
	/**五个线程串行执行 A->B->C->D->E
	 * functionDE中CompletableFuture 执行依赖functionCD中CompletableFuture执行完毕数据
	 * functionCD中CompletableFuture 执行依赖functionBC中CompletableFuture执行完毕数据
	 * functionBC中CompletableFuture 执行依赖functionAB中CompletableFuture执行完毕数据
	 * functionAB中CompletableFuture依赖futureA 执行完毕数据
	 * @param futureA
	 * @param functionAB
	 * @param functionBC
	 * @param functionCD
	 * @param functionDE
	 * @return CompletableFuture<E>
	 */
	public static <A, B, C, D , E> CompletableFuture<E> sequential(CompletableFuture<A> futureA,
			Function<A, CompletableFuture<B>> functionAB, Function<B, CompletableFuture<C>> functionBC,
			Function<C, CompletableFuture<D>> functionCD , Function<D , CompletableFuture<E>> functionDE) {

		return futureA.thenCompose(functionAB).thenCompose(functionBC).thenCompose(functionCD).thenCompose(functionDE) ;
	}
    
	
	
	/**
	 * 当所有线程执行完毕后 回收所有线程执行的结果
	 * @param cfs
	 * @return CompletableFuture<List<T>>
	 */
	public static <T> CompletableFuture<List<T>> allOf(CompletableFuture<T>... cfs) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(cfs);
        return allDoneFuture.thenApply(it ->
            Arrays.stream(cfs).
                map(future -> future.join()).
                collect(Collectors.toList())
        );
    }
	

	/**
	 * 其中任何一个线程结束后立即返回
	 * @param cfs
	 * @return CompletableFuture<T>
	 */
	@SuppressWarnings("unchecked")
	public static <T> CompletableFuture<T> anyOf(CompletableFuture<T>... cfs) {
		CompletableFuture<T> anyOfDone = (CompletableFuture<T>) CompletableFuture.anyOf(cfs) ;
		return anyOfDone ;
    }
	
	
	/**
	 * futureA |futureB 两个并行执行
	 * @param futureA
	 * @param futureB
	 * @return CompletableFuture<Tuple2<A, B>>
	 */
	public static <A, B> CompletableFuture<Tuple2<A, B>> parallel(CompletableFuture<A> futureA,
			CompletableFuture<B> futureB) {

		return futureA.thenCombine(futureB, (a, b) -> new Tuple2<>(a, b));
	}
    /**
     * futureA|futureB|futureC 三个线程并行执行
     * @param futureA
     * @param futureB
     * @param futureC
     * @return CompletableFuture<Tuple3<A, B, C>>
     */
	public static <A, B, C> CompletableFuture<Tuple3<A, B, C>> parallel(CompletableFuture<A> futureA,
			CompletableFuture<B> futureB, CompletableFuture<C> futureC) {

		return futureA.thenCombine(futureB, (a, b) -> new Tuple2<>(a, b)).thenCombine(futureC,
				(tuple2, c) -> new Tuple3<>(tuple2.a(), tuple2.b(), c));
	}
	/**
     * futureA|futureB|futureC|futureD 四个线程并行执行
     * @param futureA
     * @param futureB
     * @param futureC
     * @param futureD
     * @return CompletableFuture<Tuple4<A, B, C, D>>
     */
	public static <A, B, C, D> CompletableFuture<Tuple4<A, B, C, D>> parallel(CompletableFuture<A> futureA,
			CompletableFuture<B> futureB, CompletableFuture<C> futureC, CompletableFuture<D> futureD) {

		return futureA.thenCombine(futureB, (a, b) -> new Tuple2<>(a, b))
				.thenCombine(futureC, (tuple2, c) -> new Tuple3<>(tuple2.a(), tuple2.b(), c))
				.thenCombine(futureD, (tuple3, d) -> new Tuple4<>(tuple3.a(), tuple3.b(), tuple3.c(), d));
	}
	/**
     * futureA|futureB|futureC|futureD|futureE 五个线程并行执行
     * @param futureA
     * @param futureB
     * @param futureC
     * @param futureD
     * @param futureE
     * @return CompletableFuture<Tuple5<A, B, C, D , E>>
     */
	public static <A, B, C, D , E> CompletableFuture<Tuple5<A, B, C, D , E>> parallel(CompletableFuture<A> futureA,
			CompletableFuture<B> futureB, CompletableFuture<C> futureC, CompletableFuture<D> futureD ,
			CompletableFuture<E> futureE ) {

		return futureA.thenCombine(futureB, (a, b) -> new Tuple2<>(a, b))
				.thenCombine(futureC, (tuple2, c) -> new Tuple3<>(tuple2.a(), tuple2.b(), c))
				.thenCombine(futureD, (tuple3, d) -> new Tuple4<>(tuple3.a(), tuple3.b(), tuple3.c(), d))
				.thenCombine(futureE, (tuple4, e) -> new Tuple5<>(tuple4.a(), tuple4.b(), tuple4.c(), tuple4.d(), e));
	}
	
	//timeout
	public static <T> CompletableFuture<T> failAfter( ScheduledExecutorService scheduler, Duration duration) {
	    final CompletableFuture<T> promise = new CompletableFuture<>();
	    scheduler.schedule(() -> {
	        final TimeoutException ex = new TimeoutException("Timeout after " + duration);
	        return promise.completeExceptionally(ex);
	    }, duration.toMillis(), TimeUnit.MILLISECONDS);
	    return promise;
	}
	public static <T> CompletableFuture<T> within(CompletableFuture<T> future, ScheduledExecutorService scheduler ,Duration duration) {
	    final CompletableFuture<T> timeout = failAfter(scheduler ,  duration);
	    return future.applyToEither(timeout, Function.identity());
	}
	
}
