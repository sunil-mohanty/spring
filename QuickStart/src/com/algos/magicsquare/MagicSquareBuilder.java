package com.algos.magicsquare;

import java.util.Scanner;

public class MagicSquareBuilder {
	static Scanner scan;
	
	public static void main(String[] args) {
		System.out.println("Please enter Magic Square Order :");
		scan = new Scanner(System.in);
		
		int order = scan.nextInt();
		int magicSquare[][] = new int[order][order];
		while (order <= 2) {
			System.out.println("Please enter the Order greater than 2  :");
			order = scan.nextInt();
		}
		
		if (order % 2 == 0 ) {
			
			// Form Even order magic square
			magicSquare = MagicSquareEvenOrder.generateMagicSquare(order);
		} else {
			
			// Form Odd order magic square
			magicSquare = MagicSquareOddOrder.generateMagicSquare(order);
		}
		
		
		for (int i = 0; i<order; i++) {
			for (int j = 0; j<order; j++) {
				System.out.print(magicSquare[i][j]);
				System.out.print(" ");
			}
			System.out.println(" ");
		}
		
		int sum = (order*(order*order +1))/2;
		System.out.println("Any row or any column or any diagonal addition of this magicSquare will be : " + sum);
		System.out.println("");
		System.out.println("is this a MagicSquare ? " + validate(magicSquare, order, sum));
		
		System.out.println("");
		System.out.println("Do you want to see 4 magic squares for this same order ? y/n :");
		
		if (scan.next().equalsIgnoreCase("y")) {

			for (int i = 0; i < order; i++) {
				for (int j = 0; j < order; j++) {
					System.out.print(magicSquare[i][j] + " ");
				}
				System.out.println(" ");
			}
			
			System.out.println("\n");
			
			for (int i = 0; i < order; i++) {
				for (int j = 0; j < order; j++) {
					System.out.print(magicSquare[order - 1 - i][order - 1 - j] + " ");
				}
				System.out.println(" ");
			}

			System.out.println("\n");
			
			for (int i = 0; i < order; i++) {
				for (int j = 0; j < order; j++) {
					System.out.print(magicSquare[order - 1 - i][j] + " ");
				}
				System.out.println(" ");
			}

			System.out.println("\n");
			
			for (int i = 0; i < order; i++) {
				for (int j = 0; j < order; j++) {
					System.out.print(magicSquare[i][order - 1 - j] + " ");
				}
				System.out.println(" ");
			}
		}
		
	}
	
	public static boolean validate(int matrix[][], int order,  int val) {
		
		int firstDiagonal = 0;
		int secondDiagonal = 0;
		
		
		for (int i = 0; i<order; i++) {
			int rowRes = 0;
			int columnRes = 0;
			
			for (int j = 0; j<order; j++) {
				rowRes = rowRes + matrix[i][j];
				columnRes = columnRes + matrix[j][i]; 
				if (i == j) {
					firstDiagonal = firstDiagonal + matrix[i][j];
				}
				
				if (order-i == order-j){
					secondDiagonal = secondDiagonal + matrix[i][j];
				}
			}
			if ( rowRes !=val){
				return false;
			}
			
			if ( columnRes !=val){
				return false;
			}
		}
		
		if ( firstDiagonal !=val){
			System.out.println("failed for firstDiagonal" );
			return false;
		}
		
		if ( secondDiagonal !=val){
			System.out.println("failed for secondDiagonal" );
			return false;
		}
		
		return true;
	}

}
