package dataStructures;

import java.util.ArrayList;
import java.util.Collections;

import java.util.Random;


public class Test {
	public static void main(String args[]) {
		int n = 100;
		Random rand = new Random(12);
		SortedList<Integer> list = new SortedList<Integer>();
		long start1 = System.nanoTime();
		for(int i = 0; i< n; i++) {
			list.add(rand.nextInt(10));
		}
		long time1 = System.nanoTime() - start1;
		Random rand2 = new Random(12);
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		long start2 = System.nanoTime();
		for(int i = 0; i< n; i++) {
			list2.add(rand2.nextInt(10));
			Collections.sort(list2);
		}
		long time2 = System.nanoTime() - start2;
		System.out.printf("Alkioita: %d\nListalla 1 kului yhden lisäämiseen aikaa: %.3f ms\nListalla 2 kului näiden lisäämiseen aikaa: %.3f ms", n,(double)(time1)/(1000000*n), (double)(time2)/(1000000*n));
	}
}
