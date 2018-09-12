package ass1;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/** 
 * I think this system works to test the java garbage collector, but an 
 * alternative performance implementation should really test how this behaves
 * with consideration to different thread availability, different processors etc.
 * 
 * Also it would be far better if we used arrays rather than lists for this assignment.
 * */

public class TestPerformance {
	
	/** 
	 * This method takes some runnable, in this case the various merge sort algorithms,
	 * and attempts to run them repeatedly. First as a warm up run and again as a proper run, 
	 * then returns the time it took for the proper run to take place..
	 * */
  long timeOf(Runnable r,int warmUp,int runs) {
    System.gc();
    for(int i=0;i<warmUp;i++) {r.run();}
    long time0=System.currentTimeMillis();
    for(int i=0;i<runs;i++) {r.run();}
    long time1=System.currentTimeMillis();
    return time1-time0;
  }

  /** 
   * msg method uses the timeOf method and runs it over an entire data set.
   * It then returns the time it took to sort that whole data set 200 times after having run it 20,000 times.
   * It seems to be designed to severely push the garbage collection system used by Java,
   * potentially causing memory fragmentation issues.
   * */
  <T extends Comparable<? super T>>void msg(Sorter s,String name,T[][] dataset) {
    long time=timeOf(()->{
      for(T[]l:dataset){s.sort(Arrays.asList(l));}
      },20000,200);//realistically 20,000 to make the JIT do his job..
      System.out.println(name+" sort takes "+time/1000d+" seconds");
    }
  
  
  /** 
   * Runs the message method on the various types of merge sort.
   * */
  <T extends Comparable<? super T>>void msgAll(T[][] dataset) {
    //msg(new ISequentialSorter(),"Sequential insertion",TestBigInteger.dataset);//so slow
    //uncomment the former line to include performance of ISequentialSorter
    msg(new MSequentialSorter(),"Sequential merge sort",dataset);
    msg(new MParallelSorter1(),"Parallel merge sort (futures)",dataset);
    msg(new MParallelSorter2(),"Parallel merge sort (forkJoin)",dataset);
    }
  /** 
   * Runs all the msgAll on four different testSets. It allows us to analyze the algorithm for differences in 
   * processing time for different types of objects.
   * */
  @Test
  void testBigInteger() {
    System.out.println("On the data type BigInteger");
    msgAll(TestBigInteger.dataset);
    }
  @Test
  void testFloat() {
    System.out.println("On the data type Float");
    msgAll(TestFloat.dataset);
    }
  @Test
  void testPoint() {
    System.out.println("On the data type Point");
    msgAll(TestPoint.dataset);
    }
  @Test
  void testStrings() {
	System.out.println("On the data type String");
	msgAll(TestFloat.dataset);
  	}
  }