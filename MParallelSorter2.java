package ass1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**	Task 3: 
 * 	
 * Benefits of this implementation of Mergesort with respect to others?
 * This fork-join version of mergesort is as fast, at times faster than the default sequential mergesort.
 * It is really quick, in spite of the list manipulation, and would definitely be even faster using arrays manipulation instead.
 * 
 * 
 * What did I learn?
 * One way of implementing a concurrent algorithm may not work for a different concurrency structure.
 * 
 * */

public class MParallelSorter2 implements Sorter {
	
	/** 
	 * This method is the external access method for the forkjoin based mergesort.
	 **/
	private static ExecutorService pool;
	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		pool = new ForkJoinPool();
		FJMS begin = new FJMS(list, 0, list.size());
		begin.compute();
		return list;
	}

	private class FJMS<T extends Comparable<? super T>> extends RecursiveAction{//ForkJoin MergeSort.
		List<T> orig;
		int lb, ub;//lb is lower bound, ub is upper bound.
		
		/** 
		 * Constructor method which is required by RecursiveAction. Simply takes the list, lower and upper bounds.
		 * */
		protected FJMS(List<T> list, int l, int u) {
			orig = list;
			lb = l;
			ub = u;
		}
		
		/** 
		 * Effectively does the splitting part of mergesort. Ensures the list isn't too short,
		 * delegates the first part and computes the second. It never runs into problems due to the 
		 * work stealing nature of ForkJoinPool.
		 **/
		@Override
		protected void compute() {
			//System.out.println("Computing");
			if(lb >= ub) {
				return;
			}
			int mid = (ub+lb)/2;
			FJMS left = new FJMS(orig, lb, mid);
			FJMS right = new FJMS(orig, mid+1, ub);
			left.fork();
			right.compute();
			left.join();
			merge(orig, lb, mid, ub-1);
		}
		
		/** 
		 * Exactly the same merge as I used in the futures based mergesort.
		 * Turns out to be as flexible for bottom up as it is for top down mergesort.
		 **/
		private <T extends Comparable<? super T>> void merge(List<T> orig, int start, int mid, int end){
			//System.out.println("Merge occured");
			List<T> lesser = new ArrayList<T>();
			List<T> greater = new ArrayList<T>();
			for(int k = start; k <= mid; k++) {//build an auxiliary list off which to perform the changes.
				  lesser.add(orig.get(k));
			}
			for(int y = mid+1; y <= end; y++) {//build an auxiliary list off which to perform the changes.
				  greater.add(orig.get(y));
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
		}

	}
	
}
