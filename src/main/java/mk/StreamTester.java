package mk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mk.obj.Fruit;
import mk.obj.Fruit2;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

public class StreamTester extends JavaStreamsTesterAbstract{

	private static Logger log = LoggerFactory.getLogger(StreamTester.class);

	public static void consumerTester3() throws InterruptedException {

		// Consumer to multiply 2 to every integer of a list
		Consumer<List<Integer>> modify = list -> {
			for (int i = 0; i < list.size(); i++)
				list.set(i, 2 * list.get(i));
		};

		// Consumer to display a list of integers
		Consumer<List<Integer>> dispList = list -> list.stream().forEach(a -> System.out.print(a + " "));
//        log.info(); 

		List<Integer> list = new ArrayList<Integer>();
		list.add(2);
		list.add(1);
		list.add(3);

		// using addThen()
		try {
			log.info("A1");
			// dispList.andThen(modify).accept(list);
			dispList.andThen(modify).accept(list);

			log.info("A2");
			modify.andThen(dispList).accept(list);
			;
		} catch (Exception e) {
			log.info("Exception: " + e);
		}
	}

	public static void consumerTester2() throws InterruptedException {

		// Consumer to multiply 2 to every integer of a list
		Consumer<List<Integer>> modify = list -> {
			for (int i = 0; i < list.size(); i++)
				list.set(i, 2 * list.get(i));
		};

		// Consumer to display a list of integers
		Consumer<List<Integer>> dispList = list -> list.stream().forEach(a -> System.out.print(a + " "));

		List<Integer> list = new ArrayList<Integer>();
		list.add(2);
		list.add(1);
		list.add(3);

		// using addThen()
		modify.andThen(dispList).accept(list);
		;
	}

	public static void consumerTester() throws InterruptedException {
		// Consumer to display a number
		Consumer<Integer> display = a -> log.info("" + a);

		// Implement display using accept()
		display.accept(10);

		// Consumer to multiply 2 to every integer of a list
		Consumer<List<Integer>> modify = list -> {
			for (int i = 0; i < list.size(); i++)
				list.set(i, 2 * list.get(i));
		};

		// Consumer to display a list of numbers
		Consumer<List<Integer>> dispList = list -> list.stream().forEach(a -> log.info(">[" + a + "]"));

		List<Integer> list = new ArrayList<Integer>();
		list.add(2);
		list.add(1);
		list.add(3);

		log.info("xxx");
		// Implement modify using accept()
		modify.accept(list);
		log.info("yyy");

		// Implement dispList using accept()
		dispList.accept(list);
	}

	public static void testzipStream() throws InterruptedException {

		List<String> elements = new ArrayList<>();

		Flux.just(1, 2, 3, 4).log().map(i -> i * 2)
				.zipWith(Flux.range(0, Integer.MAX_VALUE).map(i -> i * 20),
						(one, two) -> String.format("First Flux: %d, Second Flux: %d", one, two))
				.subscribe(elements::add);

		log.info("Listing");

		for (String item : elements) {
			log.info(item);
		}

	}

	public static void testhotStreamWithProcessor() throws InterruptedException {
		log.info("*********Calling hotStream************");
		UnicastProcessor<String> hotSource = UnicastProcessor.create();

		Flux<String> hotFlux = hotSource.publish().autoConnect().map(String::toUpperCase);

		hotFlux.subscribe(d -> log.info("Subscriber 1 to Hot Source: " + d));

		hotSource.onNext("ram");
		hotSource.onNext("sam");

		hotFlux.subscribe(d -> log.info("Subscriber 2 to Hot Source: " + d));

		hotSource.onNext("dam");
		hotSource.onNext("lam");
		hotSource.onComplete();
		log.info("-------------------------------------");
	}

	public static void testhotStream3() throws InterruptedException {

		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>testhotStream3>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Flux<Integer> source = Flux.range(1, 30).doOnSubscribe(

				(s) -> {
					log.info("subscribed to source " + s);
//                				s.request(1);
				}

		)
		// .subscribeOn(Schedulers.elastic())
		;

		ConnectableFlux<Integer> co = source.publish();

		co.subscribe(d -> log.info("Subscriber 1 to Hot Source: " + d));
		co.subscribe(d -> log.info("Subscriber 2 to Hot Source: " + d));
//co.subscribe(System.out::println, e -> {}, () -> {});

//co.subscribe(StreamTester::doSomething, e -> {}, () -> {});

		log.info("done subscribing");
		Thread.sleep(500);
		log.info("will now connect");

		co.connect();
	}

	public static void doSomething(String in) throws InterruptedException {
		log.info(in);
	}

	public static void testhotStream2() throws InterruptedException {

		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>testhotStream2>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
			log.info("before while");

			boolean exitcondition = true;
			while (exitcondition) {
				log.info("Publishing");
				fluxSink.next(new Date());
				log.info("Published");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			log.info("after while");

		}).doOnSubscribe(s -> log.info("subscribed to source")).subscribeOn(Schedulers.elastic()).publish()

		;

		publish.subscribe(s -> log.info("received :" + s));
		publish.subscribe(s -> log.info("received :" + s));

		log.info(
				">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>testhotStream2 BEFORE CONNECT>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		publish.connect();

		Thread.sleep(100);

		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>testhotStream2 END>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	public static void fibonacciStream() throws InterruptedException {

		Flux<Long> fibonacciGenerator = Flux.generate(() -> Tuples.<Long, Long>of(0L, 1L), (state, sink) -> {
			sink.next(state.getT1());
			return Tuples.of(state.getT2(), state.getT1() + state.getT2());
		});

		fibonacciGenerator.take(10).collectList().subscribe(t -> {
			log.info("" + t);
		});

		Flux<Tuple2<Long, Object>> fibonacciGenerator2 = Flux
				.generate(() -> Tuples.<Long, Long>of(0L, 1L), (state, sink) -> {
					sink.next(state.getT1());
					return Tuples.of(state.getT2(), state.getT1() + state.getT2());
				}).index();

		/*
		 * fibonacciGenerator2.take(10).collectList().subscribe(t -> { log.info(
		 * t.get(0).getT1() ); });
		 */
		fibonacciGenerator2.subscribe(t -> {
			log.info(t.getT1() + " / " + t.getT2());

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

	public static void testhotStream() throws InterruptedException {

		AtomicInteger counter = new AtomicInteger();

		Flux<Object> flux = Flux.create(fluxSink -> {
			while (counter.incrementAndGet() <= 10) {
				try {

					log.info("Producer Sleeping");
					TimeUnit.MILLISECONDS.sleep(1000);
				} catch (InterruptedException ignored) {
				}
				fluxSink.next(System.currentTimeMillis());
			}
		}).log().publish().autoConnect();

		// first subscriber
		new Thread(() -> {
			log.info("[S] Subscribing first");
			flux.log().subscribe(s -> log.info("[*] S1: {}", s));
		}).start();

		Thread.sleep(450);

		// second subscriber
		new Thread(() -> {
			log.info("[S] Subscribing second");
			flux.log().subscribe(s -> log.info("[*] S2: {}", s));
		}).start();
	}

	public static void simpleTest() {

		List<Integer> elements = new ArrayList<>();

		Flux.just(1, 2, 3, 4).log().map(i -> i * 2)

				.doAfterTerminate(System.out::println).subscribe(elements::add);

		Flux<String> source = Flux.just("a", "b", "c");
		source.subscribe(new Subscriber<String>() {
			private Subscription subscription;

			@Override
			public void onSubscribe(Subscription subscription) {
				this.subscription = subscription;
				subscription.request(1); // <-- here
			}

			@Override
			public void onNext(String s) {
				subscription.request(1); // <-- here
				log.info(s);
			}

			@Override
			public void onError(Throwable t) {
			}

			@Override
			public void onComplete() {
			}

		});
		log.info("Listing");

		for (Integer item : elements) {
			log.info(item.toString());
		}

	}

	public static void main(String[] args) throws InterruptedException {

		// StreamTester.simpleTest();
		// StreamTester.testhotStream();
		// StreamTester.testhotStreamWithProcessor();
		// StreamTester.testhotStream2();
		// StreamTester.testhotStream3();
		// StreamTester.testzipStream();
		// StreamTester.fibonacciStream();
		// StreamTester.consumerTester();
		// StreamTester.consumerTester2();
		// StreamTester.consumerTester3();
		// StreamTester.javaStreamsTester();
		// StreamTester.javaStreamsTester2();
		StreamTester.javaStreamsTester3();
	}

}
