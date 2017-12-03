package com.demo.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @author sunilmohanty
 *
 */
class MergeSortDemo {
	

	private static Scanner scanner;

	public static void main(String args[]) {
		
		scanner = new Scanner(System.in);
		List<Integer> list = new ArrayList<>();
		
		int size  = scanner.nextInt();
		
		int counter = 0;
		
		while(counter < size) {
			list.add(scanner.nextInt());
			counter ++;
		}
		
		int[] arr = list.stream().mapToInt(Integer::intValue).toArray();
		
		//int arr[] = { 3, 7, 4, 2, 5, 1, 9, 8, 6 };

		System.out.println("Given Array");
		for (int i : arr) {
			System.out.print(i + "\t");
		}

		MergeSortDemo ob = new MergeSortDemo();
		ob.sort(arr, 0, arr.length - 1);

		System.out.println("\n Sorted Output");
		for (int i : arr) {
			System.out.print(i + "\t");
		}
	}


	void merge(int arr[], int l, int m, int r) {
		
		// Calculate the temporary array size to be merged
		int n1 = m - l + 1;
		int n2 = r - m;

		/* Create temp arrays */
		int temp1[] = new int[n1];
		int temp2[] = new int[n2];

		/* Copy data to temp arrays */
		for (int i = 0; i < n1; ++i) {
			temp1[i] = arr[l + i];
		}
		
		for (int j = 0; j < n2; ++j) {
			temp2[j] = arr[m + 1 + j];
		}

		/* Merge the temp arrays */

		// Initial indexes of the temp arrays 
		int x =0, y = 0;

		// Initial index of merged array
		int k = l;
		while (x < n1 && y < n2) {
			if (temp1[x] <= temp2[y]) {
				arr[k] = temp1[x];
				x++;
			} else {
				arr[k] = temp2[y];
				y++;
			}
			k++;
		}

		/* Copy remaining elements of temp1[] array if any */
		while (x < n1) {
			arr[k] = temp1[x];
			x++;
			k++;
		}

		/* Copy remaining elements of temp2[] array if any */
		while (y < n2) {
			arr[k] = temp2[y];
			y++;
			k++;
		}
	}

	void sort(int arr[], int l, int r) {
		if (l < r) {
			int m = (l + r) / 2;
			
			// Sort first half
			sort(arr, l, m);
			
			//sort the second half
			sort(arr, m + 1, r);
			// Merge the sorted halves
			merge(arr, l, m, r);

		}
	}
}