package ass1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**	Task 3: 
 * 	
 * Benefits of this implementation of Mergesort with respect to others?
 * This version of Mergesort has one major advantage which is that it is relatively
 * low in memory usage. It effectively uses its original list as well as two other lists
 * and as such it would suit a device with limited memory stack such as an embedded system if it was
 * implemented using arrays rather than lists.
 * This becomes far better when a list is used to keep futures until they need to be retrieved,
 * which largely prevents the delays waiting for retrievals.
 * 
 * What did I learn?
 * You need to think carefully about which algorithm you implement when using concurrency.
 * I originally implemented top down merge sort, however it would deadlock or starve. 
 * This implementation using futures requires bottom up. I also learned that orientating code around letting
 * futures complete before retrieving them prevents delays waiting for computation to complete.
 * 
 * */

public class MParallelSorter1 implements Sorter {
	
	private static ExecutorService pool;
	/** 
	 * This method is the external access method for the futures based merge sort algorithm.
	 * */
	
	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		 pool = Executors.newFixedThreadPool(10);
		 List<T> finished = split(list);
		 return finished;
	}
	
	/** 
	 * This is the method around which the internal futures behaviour should take place.
	 * 
	 *	This basically creates two lists, one greater list and one smaller list.
	 *	It fills each with half the data it needs, then compares while merging.
	 * */
	private <T extends Comparable<? super T>> Boolean merge(List<T> orig, int start, int mid, int end){
		List<T> lesser = new ArrayList<T>();
		List<T> greater = new ArrayList<T>();
		for(int k = start; k <= mid; k++) {//build an auxiliary list off which to perform the changes.
			  lesser.add(orig.get(k));
		}
		for(int k = mid+1; k <= end; k++) {//build an auxiliary list off which to perform the changes.
			  greater.add(orig.get(k));
		}
		int indexA = 0, indexB = 0, indexList = start;
		while(indexA < lesser.size() && indexB < greater.size()){//Sorts the two lists together until one list runs out.
			if(lesser.get(indexA).compareTo(greater.get(indexB)) < 0){
				orig.set(indexList++, lesser.get(indexA++));
			}
			else{
				orig.set(indexList++, greater.get(indexB++));
			}
		}
		while (indexA < lesser.size()) {//all that remains in listA
			orig.set(indexList++, lesser.get(indexA++));
		}
		while (indexB < greater.size()) {//all that remains in listB
			orig.set(indexList++, greater.get(indexB++));
		}
		return true;
	}
		  
	/** 
	 * This is a rather slow O(n^3) implementation of MergeSort.
	 * This method basically runs that multilayered loop which submits a future
	 * to the pool for processing.
	*/
	private <T extends Comparable<? super T>> List<T> split(List<T> orig){
		int N = orig.size();
		for(int size = 1; size < N; size *= 2){
			List<Future<Boolean>> futList = new ArrayList<Future<Boolean>>();
			for(int lo = 0; lo < N-size; lo += size+size){//This part submits the futures to be done.
				final int low = lo;
				final int sz = size;
				Future<Boolean> fut = pool.submit(() ->merge(orig, low, low+sz-1, Math.min(low+sz+sz-1, N-1)));
				futList.add(fut);
			}
			for(int u = 0; u < futList.size(); u++) {//this part does the retrieval. It more than halved the time taken to complete the task.
				retrieveFuture(futList.get(u));
			}
		}
		return orig;
	}
	
	/** 
	 * This method is supposed to provide the try-catch method. 
	 * It essentially handles the future and its completion.
	 * */
	private void retrieveFuture(Future<Boolean> receive) {
		try {
			receive.get();
		}
		catch(InterruptedException e) {
	  		Throwable t=e.getCause();//propagate unchecked exceptions
	  		t.printStackTrace();
	  	}
	  	/**catch(TimeoutException e) {
	  		Throwable t = e.getCause();
	  		t.printStackTrace();
	  	} */
	  	catch(ExecutionException e) {
	  		Throwable t=e.getCause();//propagate unchecked exceptions
	  		if(t instanceof RuntimeException) {
	  		System.out.println("RuntimeException has been thrown");
	  		throw (RuntimeException)t;
	  		}//note: CancellationException is a RuntimeException
	  		if(t instanceof Error) {
	  		System.out.println("Error has been thrown");
	  		throw (Error)t;
	  		}
	  	}
	}
}
