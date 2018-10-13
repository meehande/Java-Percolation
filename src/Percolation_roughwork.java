import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
/**
 * Percolation class 
 * - Programming assignment 1 for Princeton Algorithms 1 coursera course 
 * Solution by Deirdre Meehan in September 2018
 * 
 * This class uses unionFind to detect Percolation + does a Monte Carlo simulation to 
 * experimentally find percolation threshold
 * 
 */
public class Percolation_roughwork {

    private boolean[] grid;
    private final int gridSize;
    private final int gridSquareSize;
    private final int unionFindSize;
    private final WeightedQuickUnionUF unionFind;
    private final int virtualTopIndex;
    private final int virtualBottomIndex;
	
    private void initialiseEmptyGrid() {
		grid = new boolean[unionFindSize];
		for(int i = 0; i < grid.length ; i++) {
			grid[i] = false;
		}
		grid[virtualTopIndex] = true;
		grid[virtualBottomIndex] = true;
	}
	
	private int rowAndColToGridIndex(int row, int col) 
	{
		if(row > gridSize || col > gridSize) {
			return -1;
		}
		return ((row - 1)* gridSize + col-1) ;	
	}
	
	private boolean hasLeft(int col) {
		return col != 1;
	}
	
	private boolean hasRight(int col) {
		return col != gridSize;
	}
	
	//API METHODS
	public void open(int row, int col)    // open site (row, col) if it is not open already
	{
		int indexToOpen = rowAndColToGridIndex(row, col);
		if(tryOpenCell(indexToOpen)) {
		//openAbove
			if(isValidCell(row - 1, col) && isOpen(row - 1, col)) {
				safelyUnion(indexToOpen, indexToOpen - gridSize);
			}
			//openBelow
			if(isValidCell(row + 1, col) && isOpen(row + 1, col)) {
				safelyUnion(indexToOpen, indexToOpen + gridSize);
			}
			//openLeft
			if(isValidCell(row, col - 1) && isOpen(row, col - 1) && hasLeft(col)) {
				safelyUnion(indexToOpen, indexToOpen - 1);
			}
			//openRight
			if(isValidCell(row, col + 1) && isOpen(row, col + 1) && hasRight(col)) {
				safelyUnion(indexToOpen, indexToOpen + 1);
			}		
			//virtualTop
			if(indexToOpen < gridSize) {
				safelyUnion(indexToOpen, virtualTopIndex);
			}
			if(indexToOpen >= gridSquareSize - gridSize) {
				safelyUnion(indexToOpen, virtualBottomIndex);
			}
		}
		throw new IllegalArgumentException(String.format("row index %s out of bounds", indexToOpen)) ;
	}
	
	private boolean tryOpenCell(int indexToOpen) 
	{
		if(isValidCell(indexToOpen)) {
			grid[indexToOpen] = true;
			return true;
		}
		return false;
	}
	
	private boolean isValidCell(int index) 
	{
		return index >= 0 && index < gridSquareSize;
	}
	
	private boolean isValidCell(int row, int col) 
	{
		int index = rowAndColToGridIndex(row, col);
		return isValidCell(index);
	}
	
	private void safelyUnion(int p, int q) 
	{
		if(p >= 0 && p < unionFindSize && q >= 0 && q < unionFindSize) {
			unionFind.union(p, q);
		}
	}
	
   public boolean isOpen(int row, int col) throws IllegalArgumentException // is site (row, col) open?
   {
	   int index = rowAndColToGridIndex(row, col);
	   if(isValidCell(index)) {
		   return grid[index];
	   }
	   throw new IllegalArgumentException() ;

	   //throw new IllegalArgumentException(String.format("cell %s, %s which gave row index %s out of bounds", row, col, index)) ;
   }
	   
   
   public boolean isFull(int row, int col) throws IllegalArgumentException // is site (row, col) full?
   {
	   //is open and percolates through this cell
	   if (!isOpen(row, col)) {
		   return false;
	   }
	   int index = rowAndColToGridIndex(row, col);
	   return unionFind.connected(virtualTopIndex, index); // && unionFind.connected(virtualBottomIndex, index);
   }
	   
   public int numberOfOpenSites()       // number of open sites
   {
	   int numOpenSites = 0;
	   for(int i = 0; i < gridSquareSize; i++) {
		   if(grid[i]) {
			   numOpenSites ++;
		   }
	   }
	   return numOpenSites;
   }
	   
   public boolean percolates()              // does the system percolate?
   {
	   return unionFind.connected(virtualTopIndex, virtualBottomIndex);
   }


	
	public Percolation_roughwork(int n) {
		if(n <= 0 ) {
			throw new IllegalArgumentException();
		}
		
		gridSize = n;
		gridSquareSize = n*n;
		
		unionFindSize = gridSquareSize + 2;
		virtualTopIndex = gridSquareSize;
		virtualBottomIndex = gridSquareSize + 1;
		initialiseEmptyGrid();
		unionFind = new WeightedQuickUnionUF(unionFindSize);
	}
	
	
	

	public static void main(String[] args) throws FileNotFoundException {
		/*int n = 5;
		Percolation_roughwork p = new Percolation_roughwork(n);
		int row, column;
		int maxTries = n*n;
		int numTries = 0;
		
		while(numTries < maxTries && !p.percolates()) {
			row = StdRandom.uniform(1, n+1);
			column = StdRandom.uniform(1, n+1);
			System.out.println("Opening (" + row + ", " + column + ")");
			p.open(row,  column);
			numTries ++;
		}
		
		System.out.println("System percolated? " + p.percolates());
		System.out.println("Num open sites: " + p.numberOfOpenSites());
		System.out.println("Number of openings: " + numTries);*/
		
		int checkFullRow, checkFullCol;
		String fileName = "D:\\AlgsCourse\\Assignment1-Percolation-Submission\\percolation-test-data\\input6.txt";
		File file = new File(fileName);
		boolean a = file.exists();
		System.out.println(a);
		Scanner scanner = new Scanner(file);
		String testGridSize = scanner.nextLine(); 
		
		Percolation_roughwork p2 = new Percolation_roughwork(Integer.parseInt(testGridSize));
		//...
		
		while(scanner.hasNext()) {
			//String rowString = scanner.nextLine();
			//String colString = scanner.nextLine();
			
			String rowAndColJoined = scanner.nextLine();
			String[] rowAndCol = rowAndColJoined.trim().split(" ");
			checkFullRow = Integer.parseInt(rowAndCol[0]);
			checkFullCol = Integer.parseInt(rowAndCol[1]);
			p2.open(checkFullRow, checkFullCol);
			System.out.println("Opening " + checkFullRow + ", " + checkFullCol);
			System.out.println("Percolates through requested cell? " + p2.isFull(checkFullRow, checkFullCol));
		}

		scanner.close();

		
		
		/*Integer[] temp;
		List<Integer[]> testCase = p.CreateTestCase();
		for(int i=0; i<testCase.size(); i++) {
			temp = testCase.get(i);
			row = temp[0];
			column = temp[1];
			System.out.println("Opening (" + row + ", " + column + ")");
			p.open(row,  column);
			percolates = p.percolates();
			System.out.println("Percolates? " + percolates);	
		}
		
		System.out.println("System percolated? " + p.percolates());
		System.out.println("Num open sites: " + p.numberOfOpenSites());*/
		/*
		for(int i=0; i<10; i++) {
			row = StdRandom.uniform(1, n+1);
			column = StdRandom.uniform(1, n+1);
			System.out.println("Opening (" + row + ", " + column + ")");
			p.open(row,  column);
			percolates = p.percolates();
			System.out.println("Percolates? " + percolates);
		}
		*/
		

	}
	
	

}
