package ass1;

import java.util.ArrayList;
import java.util.List;
	/** 
	 * Benefits of this implementation of Mergesort with respect to others?
	 * This top down implementation of mergesort is quite quick as I was reminded of the sorting algorithms from comp103.
	 * This algorithm is a fast method for sorting items, second only to quicksort for best and average case while outperforming quick sort in the worst case.
	 * 
	 * 
	 * 
	 * What did I learn?
	 * I learned that code which works perfectly well for non-concurrency does not necessarily translate well for concurrency.
	 * I originally tried to rework this base code into my concurrent code using the split as the primary delegation. But that 
	 * was a complete disaster, with the fixed threads becoming completely consumed before the 10,000 item lists could be processed.
	 * 
	 * */
public class MSequentialSorter implements Sorter {
	
	/** 
	 * This is the main public access method for the class. It takes the given generic list and does its business.
	 * */
	 @Override
	 public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
	  list = split(list);
	  return list;
	 }

	/** 
	  This method merges the lists together again. Takes each list, checks if the items are in order, adds them appropriately.
	*/
	private <T extends Comparable<? super T>> void merge(List<T> listA, List<T> listB, List<T> list){
	  int indexA = 0, indexB = 0, indexList = 0;
	  while(indexA < listA.size() && indexB < listB.size()){//Sorts the two lists together until one list runs out.
		if(listA.get(indexA).compareTo(listB.get(indexB)) < 0){
	  		list.set(indexList++, listA.get(indexA++));
		}
		else{
			list.set(indexList++, listB.get(indexB++));
			
		}
	  }
	  while (indexA < listA.size()) {//all that remains in listA
	    list.set(indexList++, listA.get(indexA++));
	  }
	  while (indexB < listB.size()) {//all that remains in listB
	    list.set(indexList++, listB.get(indexB++));
	  }
	}
	  
	  /** 
	  	This method splits the received list in two, then calls merge on the two lists to bring them back together sorted.
	  */
	  private <T extends Comparable<? super T>> List<T> split(List<T> list){
	  	if(list.size() < 2){
	  		return list;
	  	}
	  	List<T> l1 = new ArrayList<T>();
		List<T> l2 = new ArrayList<T>();
	  	int mid = (list.size())/2;
	  	for(int i = 0; i < mid; i++) {
	  		l1.add(list.get(i));
	  	}
	  	for(int j = mid; j < list.size(); j++ ) {
	  		l2.add(list.get(j));
	  	}
	  	split(l1);
	  	split(l2);
	  	merge(l1, l2, list);
	  	return list;
	  }
}
