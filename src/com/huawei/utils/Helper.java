package com.huawei.utils;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;

public class Helper {
	public static Random rand = new Random();

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static Integer findDupicateInArray(Integer[] a) {
		for (int j = 0; j < a.length; j++) {
			for (int k = j + 1; k < a.length; k++) {
				if (a[j].equals(a[k]) && a[j] != -1 && a[k] != -1) {
					return a[j];
				}
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static int findFirst(int[] a) {
		return findFirst(a, 0);
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static int findFirstEqual(int[] a, int b) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] == b) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static int findFirstEqual(int[] a, int location, int b) {
		for (int i = location; i < a.length; i++) {
			if (a[i] == b) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static int findFirst(int[] a, int b) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static int findFirst(int[] a, int location, int b) {
		for (int i = location; i < a.length; i++) {
			if (a[i] != b) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public static int sum(int[] a) {
		int sum = 0;
		int i;
		for (i = 0; i < a.length; i++) {
			sum = sum + a[i];
		}
		return sum;
	}

	/**
	 * 
	 * @param filepath
	 */
	public static void setOutPut(String filepath) {
		PrintStream out;
		try {
			out = new PrintStream(filepath);
			System.setOut(out);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * randomInt
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int[] randInt(int min, int max, int n) {
		int[] result = new int[n];
		for (int i = 0; i < n; i++) {
			result[i] = rand.nextInt(max - min) + min;
		}

		return result;
	}

	/**
	 * return the index of minimum value
	 * 
	 * @param a
	 * @return
	 */
	public static int findMinimum(double[] a) {
		double min = Double.POSITIVE_INFINITY;
		int ind = -1;
		for (int i = 0; i < a.length; i++) {
			if (a[i] < min) {
				min = a[i];
				ind = i;
			}
		}
		return ind;
	}
}
