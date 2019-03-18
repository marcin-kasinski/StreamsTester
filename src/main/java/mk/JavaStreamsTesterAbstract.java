package mk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mk.obj.Fruit;
import mk.obj.Fruit2;

public abstract class JavaStreamsTesterAbstract {

	private static Logger log = LoggerFactory.getLogger(StreamTester.class);
	private static long counter;

	private static void wasCalled() {
		counter++;
	}

	public JavaStreamsTesterAbstract() {
		// TODO Auto-generated constructor stub
	}

	public static void javaStreamsTester() throws InterruptedException {
	
			log.info("");
	
			log.info("javaStreamsTester START");
			List<Fruit> fruits = new ArrayList();
			List<Fruit2> targetfruits = new ArrayList();
	
			for (int i = 0; i < 20; i++) {
				fruits.add(new Fruit(i, "F" + i));
			}
	
			log.info("\njavaStreamsTester Listing START");
	
			fruits.forEach(fruit -> {
				log.info("" + fruit.getClass().getName() + " / " + fruit.name);
			});
			log.info("javaStreamsTester Listing END\n");
	
			log.info("Converting START\n");
	
			Stream<Fruit2> newFruits = fruits.stream()
	
					.filter(
	
							(s) -> {
	
								log.info("s.data().toString() " + s.index);
								return s.index % 2 == 0;
							}
	
	//		    			  s -> s.data().getSpanTraceId().equals(id)
	
					)
	
					.filter(s -> s.index % 2 == 0)
	
					.map(fruit -> {
						fruit.name += "s";
						// return fruit;
						return new Fruit2(fruit.name);
					});
			log.info("Converting END\n");
	
			log.info("\njavaStreamsTester Listing target START");
	
			targetfruits = newFruits.collect(Collectors.toList());
			log.info("javaStreamsTester targetfruits.size() " + targetfruits.size());
	
			/*
			 * newFruits.forEach(fruit -> {
			 * log.info(""+fruit.getClass().getName()+" / "+fruit.name2); });
			 */
	
			targetfruits.forEach(fruit -> {
				log.info("" + fruit.getClass().getName() + " / " + fruit.name2);
			});
	
			log.info("javaStreamsTester Listing END\n");
	
			log.info("javaStreamsTester END");
	
		}

	
	public static void javaStreamsTester2() throws InterruptedException {

		Stream<String> streamGenerated = Stream.generate(() -> "element " + new Date()).limit(20);

//		Optional<String> anyElement =		streamGenerated.findAny();
//		log.info(anyElement.get());

		// long size=streamGenerated .count();
		// log.info("size "+size);

		streamGenerated

				.filter(element -> {
					log.info("filter() was called----------------------------------------------");
					return element.contains("X");
				})

				.map(

						String::toUpperCase

				)

				.map(s -> s.substring(0, s.length())

				).map((s) -> {

					log.info(s);
					return s.substring(0, s.length());
				}

				).collect(Collectors.toList())

		;

		log.info("END");
	}	

	public static void javaStreamsTester3() throws InterruptedException {

		log.info("javaStreamsTester3 executed");

		List<String> list = Arrays.asList("abc1", "abc2", "abc3");

		counter = 0;
		Stream<String> stream = list.stream().filter(element -> {
			wasCalled();
			return element.contains("2");
		});

		log.info("counter " + counter);

		List<String> arraylist = new ArrayList();
		// Stream<String> stream2
		arraylist = list.stream().filter(element -> {
			log.info("filter() was called");
			wasCalled();
			return element.contains("2");
		})

				.map(element -> {
					log.info("map() was called");
					return element.toUpperCase();
				})

				.collect(Collectors.toList());

		log.info("counter " + counter);
		log.info("arraylist {}", arraylist);

		Optional<String> stream4 = list.stream().filter(element -> {
			log.info("filter() was called");
			return element.contains("2");
		}).map(element -> {
			log.info("map() was called");
			return element.toUpperCase();
		}).findFirst();

		log.info("fruits-------------------------------->");

		Fruit fruit1 = Fruit.builder().setIndex(1).setName("My goal1").addLevel("abc1").addLevel("abc2")
				.addLevel("abc3").build();
		Fruit fruit2 = Fruit.builder().setIndex(2).setName("My goal2").addLevel("abc11").addLevel("abc22")
				.addLevel("abc33").build();
		Fruit fruit3 = Fruit.builder().setIndex(3).setName("My goal3").addLevel("abc111").addLevel("abc222")
				.addLevel("abc333").build();
		Fruit fruit4 = Fruit.builder().setIndex(3).setName("My goal4").addLevel("abc1").addLevel("abc2")
				.addLevel("abc3").build();

		List<Fruit> fruits = Arrays.asList(fruit1, fruit2, fruit3, fruit4);

		List<String> strings = fruits.stream().map(element -> {
			log.info("get name");
			return element.getName();
		})

				.collect(Collectors.toList());

		strings.forEach(name -> log.info(name));

		List<String> alllevels =

				fruits.stream()
				.distinct()
				.map(s -> s.getLevels())
				.flatMap(l -> l.stream())
				.distinct()
				.sorted()
				
				.collect(Collectors.toList());

		// .flatMap(fruit->fruit.getLevels() );
		// .collect(Collectors.toList());

		log.info("Listing Levels");
		alllevels.forEach(name -> log.info(name));

//		log.info("fruit {}", fruit);

	}

	
	
}
