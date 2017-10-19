package com.algos.magicsquare;

public class MagicSquareEvenOrder {

	public static  int[][] generateMagicSquare(int order) {
		int result[][] =  (order % 2 == 0 && order % 4 ==0) ? formDoubleEvenMagicSquare(order) : formSingleEvenMagicSquare(order);
		return result;
	}
	
	private static int[][] formSingleEvenMagicSquare(int n) {
		int result[][] = new int[n][n];
		
		int subMat1[][] = subMatrix(n/2,1);
		int subMat2[][] = subMatrix(n/2,2);
		int subMat3[][] = subMatrix(n/2,3);
		int subMat4[][] = subMatrix(n/2,4);
		
		
		result = combineMatrix(result, subMat1, n, 0);
		result = combineMatrix(result, subMat3, n, 1);
		result = combineMatrix(result, subMat4, n, 2);
		result = combineMatrix(result, subMat2, n, 3);
		
		// n = 4 x k + 2;
		int k = (n-2)/4;
		
		/**
		 * Now we will apply the below formula to the above result. 
		 *  
		 * the first k-1 columns and in the last (k-1) columns we exchange the 
		 * corresponding elements in the upper square with those in the lower 
		 * square and in the kth column we exchange all but the middle elements 
		 * of this column. Finally we exchange the middle element of the upper and 
		 * lower squares in the (k+1)st column. The result should be an nxn magic square.
		 * 
		 * if k-1= 1, then it is the 1st column i.e 0th index column
		 */
		
		for (int i = 0; i<n/2; i++) {
			for (int j =0 ;j<=k;  j++) {
				
				if ((j < (k-1)) || ((j ==k-1) && !(i ==n/4)) || ((j== k) && (i==n/4))) {
					result[i][j] = result[i][j]  ^ result[n/2+i][j];
					result[n/2+i][j] = result[i][j]  ^ result[n/2+i][j];
					result[i][j] = result[i][j]  ^ result[n/2+i][j];
				}
				
				if (j < k-1) {
					result[i][n-1-j] = result[i][n-1-j] ^ result[n/2+i][n-1-j];
					result[n/2+i][n-1-j] = result[i][n-1-j] ^ result[n/2+i][n-1-j];
					result[i][n-1-j] = result[i][n-1-j] ^ result[n/2+i][n-1-j];
				}
			}
		}
		
		
		return result;
		
		
	}
	
	private static int[][] combineMatrix(int[][] matrix, int[][]subMatrix, int order, int n){
		
		int rowAdd = 0;
		int colAdd = 0;
		switch (n) {
			case 1 :
				colAdd = order/2;
				break;
			case 2 :
				rowAdd = order/2;
				break;
			case 3 :
				rowAdd=colAdd=order/2;
				break;
			default:
				rowAdd=colAdd=0;
		}
		
		for ( int i =0; i < order/2 ; i++) {
			for (int j=0; j < order/2 ;j++){
				
				matrix[i + rowAdd][j +colAdd] = subMatrix[i][j];
			}
		}
		
		return matrix;
		
	}
	
	private static int[][] formDoubleEvenMagicSquare(int n) {
		int result[][] = new int[n][n];
		int count =0;
		for (int i=0 ; i<n; i++) {
			for (int j=0 ; j<n; j++) {
				count++;
				result[i][j] = count;
			}
		}
		result = evaluateDoubleEven(result,n,4);
		result = evaluateDoubleEven(result,n,2);
		return result;
	}
	
	private static int[][] subMatrix(int n, int index) {
		int result[][] = new int[n][n];
		index =  (index-1) * (n*n) + 1;
		
		int i = n / 2; 
		int j = n-1;
		result[i][j] = index;
		
		for (int k = 2 ; k <= n*n ; k++) {
			index = index + 1;
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
			result[i][j] = index;
			
		}
		
		return result;
	}

	private static int[][]  evaluateDoubleEven(int result[][], int n, int devider) {
		int startIndex = devider == 4 ? 0 : n/4;
		
		for (int i = startIndex; i < n/devider; i ++) {
			for (int j = startIndex ; j< n/devider ; j++) {
				// Swapping of left top corner with right bottom corner
				result[i][j] = result[i][j] ^ result[n-1-i][n-1-j];
				result[n-1-i][n-1-j] = result[i][j] ^ result[n-1-i][n-1-j];
				result[i][j] = result[i][j] ^ result[n-1-i][n-1-j];
				
				// swapping of right top corner with bottom left corner
			   result[i][n-1-j] = result[i][n-1-j] ^ result[n-1-i][j];
			   result[n-1-i][j] = result[i][n-1-j] ^ result[n-1-i][j];
			   result[i][n-1-j] = result[i][n-1-j] ^ result[n-1-i][j];
			}
		}
		return result;
	}
}
