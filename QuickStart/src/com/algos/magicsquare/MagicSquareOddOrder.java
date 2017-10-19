package com.algos.magicsquare;


public class MagicSquareOddOrder {

	public static int[][] generateMagicSquare(int order) {
		
		int [][] result = formOddOrderMagicSquare(order);
		return result;
	}
	
	
	
	private static int[][] formOddOrderMagicSquare(int n) {
		int result[][] = new int[n][n];
		int i = n / 2; 
		int j = n-1;
		result[i][j] = 1;
		
		for (int k = 2 ; k <= n*n ; k++) {
			if ((i-1 == -1 || j + 1 == n) && !((i -1 == -1) && (j +1 == n))){
				if ( i-1 == -1) {
					i = n-1;
				} else {
					i = i -1;
				}
				if (j + 1 == n) {
					j = 0;
				} else {
					j = j+1;
				}
			} else if (i -1 == -1 && j +1 == n) {
				i=0;
				j=n-2;
			} else {
				i = i-1;
				j = j+1;
				if ( result[i][j] != 0 ) {
					i = i+1;
					j = j - 2;
				}
				
			}
			result[i][j] = k;
		}
		
		return result;
	}
}
