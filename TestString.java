package ass1;

import java.math.BigInteger;
import java.util.Random;

import org.junit.jupiter.api.Test;


public class TestString {
	/** 
	 * The poem used here is as from the darkening gloom by Keats.
	 * 
	 * It makes a great test set.
	 * */
	
	public static final String[][] dataset= {
			{new String("As"),new String("from"),new String("the"),new String("darkening"),new String("gloom"), new String("a"), new String("silver"), new String("dove")},
		    {new String("Upsoars"),new String("and"),new String("darts"),new String("into"),new String("the"),new String("Eastern"), new String("light")},
		    {new String("On"),new String("pinions"),new String("that"),new String("naught"),new String("moves"),new String("but"),new String("pure"),new String("delight")},
		    {new String("So"),new String("fled"),new String("thy"),new String("soul"),new String("into"),new String("the"),new String("realms"),new String("above")},
		    {new String("Regions"),new String("of"),new String("peace"),new String("and"),new String("everlasting"),new String("love")},
		    {new String("Where"),new String("happy"),new String("spirits"),new String("crowned"),new String("with"),new String("circlets"),new String("bright")},
		    {new String("Of"), new String("starry"), new String("beam"), new String("and"), new String("gloriously"),new String("bedight")},
		    {new String("Taste"), new String("the"), new String("high"),new String("joy"),new String("none"),new String("but"),new String("the"),new String("blest"),new String("can"), new String("prove")},
		    {new String("There"), new String("thou"), new String("or"),new String("joinest"),new String("the"),new String("immortal"),new String("quire")},
		    {new String("In"), new String("melodies"), new String("that"),new String("even"),new String("Heaven"),new String("fair")},
		    {new String("Fill"), new String("with"), new String("superior"),new String("bliss"),new String("or"),new String("at"),new String("desire")},
		    {new String("Of"), new String("the"), new String("omnipotent"),new String("Father"),new String("cleavest"),new String("the"),new String("air")},
		    {new String("On"), new String("holy"), new String("message"),new String("sent"),new String("What"),new String("pleasures"),new String("higher")},
		    {new String("Wherefore"), new String("does"), new String("any"),new String("grief"),new String("our"),new String("joy"),new String("impair?")},
		    {},
		    manyOrdered(10000),
		    manyRandom(10000),
		    manyRandomCase(10000)
	};
	
	 static private String[] manyOrdered(int size) {//Number based strings
		 String[] result=new String[size];
		 for(int i=0;i<size;i++){result[i]=new String("99999"+i);}
		 return result;
	 }
	 
	 static private String[] manyRandom(int size) {//Random letters assembled, all lower case
		 String[] result = new String[size];
		 for(int j = 0; j < size; j++) {
			 result[j]=new String(getLetters());
		 }
		 return result;
	 }
	 
	 static private String[] manyRandomCase(int size) {//Random letters assembled, random case
		 String[] result = new String[size];
		 for(int j = 0; j < size; j++) {
			 result[j]=new String(getCase());
		 }
		 return result;
	 }
	 
	 private static String getLetters() {
		String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
				"n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
		};
		int size = (int) (10*Math.random());
		String result = new String();
		for(int i = 0; i < size; i++) {
			int r = (int) (26*Math.random());
			result = result+alphabet[r];
		}
		return result;
	 }
	 private static String getCase() {
			String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
					"n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", 
					"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
					"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
			};
			int size = (int) (10*Math.random());
			String result = new String();
			for(int i = 0; i < size; i++) {
				int r = (int) (52*Math.random());
				result = result+alphabet[r];
			}
			return result;
		 }
	 
	  @Test
	  public void testISequentialSorter() {
	    Sorter s=new ISequentialSorter();
	    for(String[]l:dataset){TestHelper.testData(l,s);}
	  }
	  @Test
	  public void testMSequentialSorter() {
	    Sorter s=new MSequentialSorter();
	    for(String[]l:dataset){TestHelper.testData(l,s);}
	  }
	  @Test
	  public void testMParallelSorter1() {
	    Sorter s=new MParallelSorter1();
	    for(String[]l:dataset){TestHelper.testData(l,s);}
	  }
	  @Test
	  public void testMParallelSorter2() {
	    Sorter s=new MParallelSorter2();
	    for(String[]l:dataset){TestHelper.testData(l,s);}
	  }
}